package com.acural.boothify.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.acural.boothify.R
import com.acural.boothify.model.MemberEntity
import com.acural.boothify.roomdb.AppDatabase
import java.io.ByteArrayOutputStream

class SecondStep_Fragment : Fragment() {

    // Views
    private lateinit var etVoter: EditText
    private lateinit var etOccupation: EditText
    private lateinit var etOtherEducation: EditText

    private lateinit var spEducation: Spinner

    private lateinit var layoutCapture: LinearLayout

    private lateinit var btnSubmit: Button
    private lateinit var btnBack: Button

    private lateinit var imagePreview: ImageView

    // SharedPref
    private lateinit var prefs: SharedPreferences

    // Image
    private var imageByteArray: ByteArray? = null

    // Education
    private var selectedEducation = ""

    companion object {

        const val CAMERA_REQUEST = 101

        const val GALLERY_REQUEST = 102
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_second_step_,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        setupEducationSpinner()

        setupImagePicker()

        setupButtons()
    }

    private fun initViews(view: View) {

        etVoter = view.findViewById(R.id.etVoter)

        etOccupation = view.findViewById(R.id.etOccupation)

        spEducation = view.findViewById(R.id.spEducation)

        layoutCapture = view.findViewById(R.id.layoutCapture)

        btnSubmit = view.findViewById(R.id.btnSubmit)

        btnBack = view.findViewById(R.id.btnBack)

        // Create dynamically
        etOtherEducation = EditText(requireContext())

        etOtherEducation.hint = "Enter Qualification"

        etOtherEducation.visibility = View.GONE

        etOtherEducation.setBackgroundResource(R.drawable.edit_bg)

        etOtherEducation.setPadding(30, 20, 30, 20)

        // Add dynamically below spinner
        (spEducation.parent as LinearLayout)
            .addView(etOtherEducation)

        // Image Preview
        imagePreview = ImageView(requireContext())

        imagePreview.layoutParams =
            LinearLayout.LayoutParams(
                250,
                250
            )

        imagePreview.scaleType =
            ImageView.ScaleType.CENTER_CROP

        imagePreview.visibility = View.GONE

        layoutCapture.addView(imagePreview)

        prefs = requireActivity()
            .getSharedPreferences(
                "BoothifyPrefs",
                Context.MODE_PRIVATE
            )
    }

    // ─────────────────────────────────────
    // EDUCATION SPINNER
    // ─────────────────────────────────────
    private fun setupEducationSpinner() {

        val educationList = arrayListOf(

            "Select Qualification",

            "10th",

            "12th",

            "Graduate",

            "PG",

            "Any Other"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            educationList
        )

        spEducation.adapter = adapter

        spEducation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedEducation =
                        educationList[position]

                    if (selectedEducation == "Any Other") {

                        etOtherEducation.visibility =
                            View.VISIBLE

                    } else {

                        etOtherEducation.visibility =
                            View.GONE
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {

                }
            }
    }

    // ─────────────────────────────────────
    // IMAGE PICKER
    // ─────────────────────────────────────
    private fun setupImagePicker() {

        layoutCapture.setOnClickListener {

            val options = arrayOf(

                "Camera",

                "Gallery"
            )

            android.app.AlertDialog.Builder(
                requireContext()
            )
                .setTitle("Choose Image")
                .setItems(options) { _, which ->

                    if (which == 0) {

                        val intent = Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                        )

                        startActivityForResult(
                            intent,
                            CAMERA_REQUEST
                        )

                    } else {

                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(
                            intent,
                            GALLERY_REQUEST
                        )
                    }
                }
                .show()
        }
    }

    // ─────────────────────────────────────
    // IMAGE RESULT
    // ─────────────────────────────────────
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {

                val bitmap =
                    data?.extras?.get("data") as Bitmap

                imagePreview.visibility = View.VISIBLE

                imagePreview.setImageBitmap(bitmap)

                imageByteArray =
                    bitmapToByteArray(bitmap)
            }

            else if (requestCode == GALLERY_REQUEST) {

                val uri: Uri? = data?.data

                val bitmap =
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        uri
                    )

                imagePreview.visibility = View.VISIBLE

                imagePreview.setImageBitmap(bitmap)

                imageByteArray =
                    bitmapToByteArray(bitmap)
            }
        }
    }

    // ─────────────────────────────────────
    // BITMAP TO BYTE ARRAY
    // ─────────────────────────────────────
    private fun bitmapToByteArray(
        bitmap: Bitmap
    ): ByteArray {

        val stream =
            ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            70,
            stream
        )

        return stream.toByteArray()
    }

    // ─────────────────────────────────────
    // BUTTONS
    // ─────────────────────────────────────
    private fun setupButtons() {

        btnBack.setOnClickListener {

            requireActivity()
                .onBackPressedDispatcher
                .onBackPressed()
        }

        btnSubmit.setOnClickListener {

            saveMember()
        }
    }

    // ─────────────────────────────────────
    // SAVE MEMBER
    // ─────────────────────────────────────
    private fun saveMember() {

        val voter =
            etVoter.text.toString().trim()

        val occupation =
            etOccupation.text.toString().trim()

        if (voter.isEmpty()) {

            etVoter.error =
                "Enter voter ID"

            return
        }

        if (occupation.isEmpty()) {

            etOccupation.error =
                "Enter occupation"

            return
        }

        if (selectedEducation ==
            "Select Qualification"
        ) {

            Toast.makeText(
                requireContext(),
                "Select qualification",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        var finalEducation =
            selectedEducation

        if (selectedEducation ==
            "Any Other"
        ) {

            finalEducation =
                etOtherEducation.text
                    .toString()
                    .trim()

            if (finalEducation.isEmpty()) {

                etOtherEducation.error =
                    "Enter qualification"

                return
            }
        }

        if (imageByteArray == null) {

            Toast.makeText(
                requireContext(),
                "Select image",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Room DB
        val db =
            AppDatabase.getInstance(
                requireContext()
            )

        val member =
            MemberEntity()

        // First Fragment Data
        member.name =
            prefs.getString("name", "")!!

        member.mobile =
            prefs.getString("mobile", "")!!

        member.father =
            prefs.getString("father", "")!!

        member.gender =
            prefs.getString("gender", "")!!

        member.dob =
            prefs.getString("dob", "")!!

        member.age =
            prefs.getString("age", "")!!

        member.block =
            prefs.getString("block", "")!!

        member.division =
            prefs.getString("division", "")!!

        member.district =
            prefs.getString("district", "")!!

        member.assembly =
            prefs.getString("assembly", "")!!

        // Second Fragment Data
        member.voterId = voter

        member.occupation = occupation

        member.education = finalEducation

//        member.image = imageByteArray

        // Save
        db.memberDao().insert(member)

        Toast.makeText(
            requireContext(),
            "Member Saved Successfully",
            Toast.LENGTH_LONG
        ).show()

        clearForm()
    }

    private fun clearForm() {

        etVoter.setText("")

        etOccupation.setText("")

        etOtherEducation.setText("")

        spEducation.setSelection(0)

        imagePreview.setImageBitmap(null)

        imagePreview.visibility = View.GONE

        imageByteArray = null
    }
}