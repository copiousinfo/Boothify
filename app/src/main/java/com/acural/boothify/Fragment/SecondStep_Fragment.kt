//package com.acural.boothify.Fragment
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.fragment.app.Fragment
//import com.acural.boothify.R
//import com.acural.boothify.UiActivity.DashboardActivity
//import com.acural.boothify.model.MemberEntity
//import com.acural.boothify.roomdb.AppDatabase
//import java.io.ByteArrayOutputStream
//import java.io.FileOutputStream
//import java.text.SimpleDateFormat
//import java.util.*
//
//class SecondStep_Fragment : Fragment() {
//
//    private lateinit var etVoter: EditText
//    private lateinit var etOccupation: EditText
//    private lateinit var etOtherEducation: EditText
//    private lateinit var spEducation: Spinner
//    private lateinit var layoutCapture: LinearLayout
//    private lateinit var btnSubmit: Button
//    private lateinit var btnBack: Button
//    private lateinit var imagePreview: ImageView
//    private lateinit var prefs: SharedPreferences
//
//    private var imageByteArray: ByteArray? = null
//    private var savedImagePath: String = ""
//    private var selectedEducation = ""
//
//    companion object {
//        const val CAMERA_REQUEST = 101
//        const val GALLERY_REQUEST = 102
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_second_step_, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initViews(view)
//        setupEducationSpinner()
//        setupImagePicker()
//        setupButtons()
//    }
//
//    // =========================
//    // INIT VIEWS
//    // =========================
//    private fun initViews(view: View) {
//        etVoter = view.findViewById(R.id.etVoter)
//        etOccupation = view.findViewById(R.id.etOccupation)
//        spEducation = view.findViewById(R.id.spEducation)
//        layoutCapture = view.findViewById(R.id.layoutCapture)
//        btnSubmit = view.findViewById(R.id.btnSubmit)
//        btnBack = view.findViewById(R.id.btnBack)
//
//        // Dynamic EditText for custom qualification
//        etOtherEducation = EditText(requireContext())
//        etOtherEducation.hint = "Enter Qualification"
//        etOtherEducation.visibility = View.GONE
//        etOtherEducation.setBackgroundResource(R.drawable.edit_bg)
//        etOtherEducation.setPadding(30, 20, 30, 20)
//        (spEducation.parent as LinearLayout).addView(etOtherEducation)
//
//        // ✅ ImageView — MATCH_PARENT dono side
//        imagePreview = ImageView(requireContext())
//        val params = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        imagePreview.layoutParams = params
//        imagePreview.scaleType = ImageView.ScaleType.CENTER_CROP
//        imagePreview.visibility = View.GONE
//        layoutCapture.addView(imagePreview)
//
//        prefs = requireActivity().getSharedPreferences("BoothifyPrefs", Context.MODE_PRIVATE)
//    }
//
//    // =========================
//    // EDUCATION SPINNER
//    // =========================
//    private fun setupEducationSpinner() {
//        val educationList = arrayListOf(
//            "Select Qualification", "10th", "12th",
//            "Graduate", "PG", "Any Other"
//        )
//
//        val adapter = object : ArrayAdapter<String>(
//            requireContext(),
//            android.R.layout.simple_spinner_item,
//            educationList
//        ) {
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val v = super.getView(position, convertView, parent)
//                val text = v.findViewById<TextView>(android.R.id.text1)
//                text.setTextColor(android.graphics.Color.BLACK)
//                text.textSize = 14f
//                text.setPadding(8, 0, 8, 0)
//                return v
//            }
//
//            override fun getDropDownView(
//                position: Int,
//                convertView: View?,
//                parent: ViewGroup
//            ): View {
//                val v = super.getDropDownView(position, convertView, parent)
//                val text = v.findViewById<TextView>(android.R.id.text1)
//                text.setTextColor(android.graphics.Color.BLACK)
//                text.setBackgroundColor(android.graphics.Color.WHITE)
//                text.textSize = 14f
//                text.setPadding(24, 16, 24, 16)
//                return v
//            }
//        }
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spEducation.adapter = adapter
//
//        spEducation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?, view: View?, position: Int, id: Long
//            ) {
//                (view as? TextView)?.setTextColor(android.graphics.Color.BLACK)
//                selectedEducation = educationList[position]
//
//                if (selectedEducation == "Any Other") {
//                    showQualificationDialog()
//                } else {
//                    etOtherEducation.visibility = View.GONE
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//    }
//
//    // =========================
//    // QUALIFICATION DIALOG
//    // =========================
//    private fun showQualificationDialog() {
//        val ctx = requireContext()
//
//        val root = LinearLayout(ctx)
//        root.orientation = LinearLayout.VERTICAL
//        root.setBackgroundColor(android.graphics.Color.WHITE)
//
//        // Header
//        val header = LinearLayout(ctx)
//        header.orientation = LinearLayout.VERTICAL
//        header.gravity = android.view.Gravity.CENTER
//        header.setBackgroundColor(android.graphics.Color.parseColor("#1E3A5F"))
//        header.setPadding(40, 50, 40, 50)
//
//        val icon = TextView(ctx)
//        icon.text = "🎓"
//        icon.textSize = 36f
//        icon.gravity = android.view.Gravity.CENTER
//
//        val title = TextView(ctx)
//        title.text = "Enter Qualification"
//        title.setTextColor(android.graphics.Color.parseColor("#FFD700"))
//        title.textSize = 18f
//        title.typeface = android.graphics.Typeface.DEFAULT_BOLD
//        title.gravity = android.view.Gravity.CENTER
//        val titleParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        titleParams.topMargin = 12
//        title.layoutParams = titleParams
//
//        val subtitle = TextView(ctx)
//        subtitle.text = "Type your custom qualification below"
//        subtitle.setTextColor(android.graphics.Color.parseColor("#94A3B8"))
//        subtitle.textSize = 12f
//        subtitle.gravity = android.view.Gravity.CENTER
//        val subParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        subParams.topMargin = 6
//        subtitle.layoutParams = subParams
//
//        header.addView(icon)
//        header.addView(title, titleParams)
//        header.addView(subtitle, subParams)
//
//        // Gold Divider
//        val divider = android.view.View(ctx)
//        divider.setBackgroundColor(android.graphics.Color.parseColor("#FFD700"))
//        divider.layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT, 4
//        )
//
//        // Body
//        val body = LinearLayout(ctx)
//        body.orientation = LinearLayout.VERTICAL
//        body.setPadding(60, 50, 60, 30)
//
//        val inputLabel = TextView(ctx)
//        inputLabel.text = "QUALIFICATION"
//        inputLabel.textSize = 10f
//        inputLabel.setTextColor(android.graphics.Color.parseColor("#9CA3AF"))
//        inputLabel.typeface = android.graphics.Typeface.DEFAULT_BOLD
//        inputLabel.letterSpacing = 0.1f
//
//        val editText = EditText(ctx)
//        editText.hint = "e.g. B.Tech, MBA, PhD..."
//        editText.setHintTextColor(android.graphics.Color.parseColor("#9CA3AF"))
//        editText.setTextColor(android.graphics.Color.parseColor("#1F2937"))
//        editText.textSize = 15f
//        editText.typeface = android.graphics.Typeface.DEFAULT_BOLD
//        editText.setPadding(30, 30, 30, 30)
//        editText.setBackgroundResource(android.R.drawable.edit_text)
//        val editParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        editParams.topMargin = 12
//
//        body.addView(inputLabel)
//        body.addView(editText, editParams)
//
//        // Button Row
//        val btnRow = LinearLayout(ctx)
//        btnRow.orientation = LinearLayout.HORIZONTAL
//        btnRow.gravity = android.view.Gravity.END
//        btnRow.setPadding(40, 20, 40, 40)
//
//        val btnCancel = Button(ctx)
//        btnCancel.text = "Cancel"
//        btnCancel.setTextColor(android.graphics.Color.parseColor("#6B7280"))
//        btnCancel.isAllCaps = false
//        btnCancel.background = null
//        btnCancel.textSize = 14f
//        btnCancel.typeface = android.graphics.Typeface.DEFAULT_BOLD
//
//        val btnSave = Button(ctx)
//        btnSave.text = "Save"
//        btnSave.setTextColor(android.graphics.Color.WHITE)
//        btnSave.isAllCaps = false
//        btnSave.textSize = 14f
//        btnSave.typeface = android.graphics.Typeface.DEFAULT_BOLD
//        val saveBg = android.graphics.drawable.GradientDrawable()
//        saveBg.shape = android.graphics.drawable.GradientDrawable.RECTANGLE
//        saveBg.cornerRadius = 20f
//        saveBg.setColor(android.graphics.Color.parseColor("#1E3A5F"))
//        btnSave.background = saveBg
//        val saveParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        btnSave.setPadding(60, 16, 60, 16)
//
//        btnRow.addView(btnCancel)
//        btnRow.addView(btnSave, saveParams)
//
//        root.addView(header)
//        root.addView(divider)
//        root.addView(body)
//        root.addView(btnRow)
//
//        val dialog = android.app.AlertDialog.Builder(ctx)
//            .setView(root)
//            .create()
//
//        dialog.window?.setBackgroundDrawable(
//            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
//        )
//        dialog.window?.setLayout(
//            (ctx.resources.displayMetrics.widthPixels * 0.92).toInt(),
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        dialog.setCancelable(false)
//
//        btnCancel.setOnClickListener {
//            spEducation.setSelection(0)
//            selectedEducation = ""
//            dialog.dismiss()
//        }
//
//        btnSave.setOnClickListener {
//            val input = editText.text.toString().trim()
//            if (input.isEmpty()) {
//                editText.error = "Please enter qualification"
//            } else {
//                selectedEducation = input
//                Toast.makeText(ctx, "Qualification: $input", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
//            }
//        }
//
//        dialog.show()
//    }
//
//    // =========================
//    // IMAGE PICKER DIALOG
//    // =========================
//    private fun showImagePickerDialog() {
//        android.app.AlertDialog.Builder(requireContext())
//            .setTitle("Choose Image")
//            .setItems(arrayOf("Camera", "Gallery")) { _, which ->
//                if (which == 0) {
//                    startActivityForResult(
//                        Intent(MediaStore.ACTION_IMAGE_CAPTURE),
//                        CAMERA_REQUEST
//                    )
//                } else {
//                    startActivityForResult(
//                        Intent(
//                            Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                        ),
//                        GALLERY_REQUEST
//                    )
//                }
//            }
//            .show()
//    }
//
//    private fun setupImagePicker() {
//        layoutCapture.setOnClickListener {
//            showImagePickerDialog()
//        }
//    }
//
//    // =========================
//    // IMAGE RESULT
//    // =========================
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode != Activity.RESULT_OK) return
//
//        try {
//            val bitmap: Bitmap? = when (requestCode) {
//
//                CAMERA_REQUEST -> {
//                    // ✅ Camera thumbnail — scale up karo
//                    val raw = data?.extras?.get("data") as? Bitmap
//                    raw?.let { cropToSquare(it) }
//                }
//
//                GALLERY_REQUEST -> {
//                    // ✅ Gallery — InputStream se load karo
//                    val uri = data?.data
//                    uri?.let {
//                        val inputStream = requireActivity().contentResolver.openInputStream(it)
//                        val raw = BitmapFactory.decodeStream(inputStream)
//                        inputStream?.close()
//                        cropToSquare(raw)
//                    }
//                }
//
//                else -> null
//            }
//
//            if (bitmap != null) {
//                // ✅ 600x600 resize
//                val finalBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true)
//
//                // ✅ Storage mein save karo
//                savedImagePath = saveImageToStorage(finalBitmap)
//                imageByteArray = bitmapToByteArray(finalBitmap)
//
//                // ✅ Preview dikhao — puri box fill
//                setImagePreview(finalBitmap)
//
//            } else {
//                Toast.makeText(requireContext(), "Image load failed", Toast.LENGTH_SHORT).show()
//            }
//
//        } catch (e: Exception) {
//            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // ✅ Square crop center se
//    private fun cropToSquare(bitmap: Bitmap): Bitmap {
//        val w = bitmap.width
//        val h = bitmap.height
//        val size = minOf(w, h)
//        val x = (w - size) / 2
//        val y = (h - size) / 2
//        return Bitmap.createBitmap(bitmap, x, y, size, size)
//    }
//
//    // ✅ Preview set karo — puri layoutCapture fill ho
//    private fun setImagePreview(bitmap: Bitmap) {
//        // Pehle sab children hata do
//        layoutCapture.removeAllViews()
//
//        val imgView = ImageView(requireContext())
//        imgView.layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        imgView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imgView.setImageBitmap(bitmap)
//
//        // ✅ Tap karke image change kar sako
//        imgView.setOnClickListener {
//            showImagePickerDialog()
//        }
//
//        layoutCapture.addView(imgView)
//        imagePreview = imgView
//    }
//
//    // =========================
//    // SAVE IMAGE TO STORAGE
//    // =========================
//    private fun saveImageToStorage(bitmap: Bitmap): String {
//        val filename = "member_${System.currentTimeMillis()}.jpg"
//        val file = java.io.File(requireContext().filesDir, filename)
//        FileOutputStream(file).use { stream ->
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
//            stream.flush()
//        }
//        return file.absolutePath
//    }
//
//    // =========================
//    // BITMAP TO BYTE ARRAY
//    // =========================
//    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
//        return stream.toByteArray()
//    }
//
//    // =========================
//    // GENERATE MEMBER ID
//    // =========================
//    private fun generateMemberId(): String {
//        val db = AppDatabase.getInstance(requireContext())
//        var memberId: String
//        do {
//            memberId = (1000..9999).random().toString()
//        } while (db.memberDao().checkMemberId(memberId) > 0)
//        return memberId
//    }
//
//    // =========================
//    // BUTTONS
//    // =========================
//    private fun setupButtons() {
//        btnBack.setOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }
//        btnSubmit.setOnClickListener {
//            saveMember()
//        }
//    }
//
//    // =========================
//    // SAVE MEMBER
//    // =========================
//    private fun saveMember() {
//
//        val voter = etVoter.text.toString().trim()
//        val occupation = etOccupation.text.toString().trim()
//
//        if (voter.isEmpty()) {
//            etVoter.error = "Enter voter ID"
//            return
//        }
//
//        if (occupation.isEmpty()) {
//            etOccupation.error = "Enter occupation"
//            return
//        }
//
//        if (selectedEducation == "Select Qualification" || selectedEducation.isEmpty()) {
//            Toast.makeText(requireContext(), "Select qualification", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (savedImagePath.isEmpty()) {
//            Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // ✅ isExistingMember bundle se lo
//        val isExisting = arguments?.getBoolean("isExistingMember", false) ?: false
//
//        val db = AppDatabase.getInstance(requireContext())
//        val member = MemberEntity()
//
//        member.memberId = generateMemberId()
//
//        // ✅ Full datetime store karo — date + time dono
//        member.createdDateTime = SimpleDateFormat(
//            "yyyy-MM-dd HH:mm:ss",
//            Locale.US
//        ).format(Date())
//
//        member.name = prefs.getString("name", "")!!
//        member.mobile = prefs.getString("mobile", "")!!
//        member.father = prefs.getString("father", "")!!
//        member.gender = prefs.getString("gender", "")!!
//        member.dob = prefs.getString("dob", "")!!
//        member.age = prefs.getString("age", "")!!
//        member.block = prefs.getString("block", "")!!
//        member.division = prefs.getString("division", "")!!
//        member.district = prefs.getString("district", "")!!
//        member.assembly = prefs.getString("assembly", "")!!
//        member.voterId = voter
//        member.occupation = occupation
//        member.education = selectedEducation
//        member.imagePath = savedImagePath
//
//        // ✅ Existing member flag set karo
//        member.isExistingMember = if (isExisting) 1 else 0
//
//        db.memberDao().insert(member)
//
//        // Success dialog same rahega...
//        val dialogView = layoutInflater.inflate(R.layout.success_dialog, null)
//        val dialog = android.app.AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(false)
//            .create()
//
//        dialog.window?.setBackgroundDrawable(
//            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
//        )
//
//        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
//        btnOk.setOnClickListener {
//            dialog.dismiss()
//            val intent = Intent(requireContext(), DashboardActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//            requireActivity().finishAffinity()
//        }
//
//        dialog.show()
//    }
//
//
//}
//
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
import com.acural.boothify.UiActivity.DashboardActivity
import com.acural.boothify.model.MemberEntity
import com.acural.boothify.roomdb.AppDatabase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class SecondStep_Fragment : Fragment() {

    private lateinit var etVoter: EditText
    private lateinit var etOccupation: EditText
    private lateinit var etOtherEducation: EditText
    private lateinit var spEducation: Spinner
    private lateinit var layoutCapture: LinearLayout
    private lateinit var btnSubmit: Button
    private lateinit var btnBack: Button
    private lateinit var imagePreview: ImageView
    private lateinit var prefs: SharedPreferences


    private lateinit var layoutVoterDoc: LinearLayout
    private lateinit var tvVoterDocName: TextView

    private var imageByteArray: ByteArray? = null
    private var savedImagePath: String = ""
    private var selectedEducation = ""

    // ✅ NEW — Voter document state
    private var savedVoterDocPath: String = ""

    companion object {
        const val CAMERA_REQUEST = 101
        const val GALLERY_REQUEST = 102
        const val VOTER_DOC_REQUEST = 103   // ✅ NEW
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_second_step_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupEducationSpinner()
        setupImagePicker()
        setupVoterDocPicker()   // ✅ NEW
        setupButtons()
    }

    private fun initViews(view: View) {
        etVoter        = view.findViewById(R.id.etVoter)
        etOccupation   = view.findViewById(R.id.etOccupation)
        spEducation    = view.findViewById(R.id.spEducation)
        layoutCapture  = view.findViewById(R.id.layoutCapture)
        btnSubmit      = view.findViewById(R.id.btnSubmit)
        btnBack        = view.findViewById(R.id.btnBack)
        layoutVoterDoc = view.findViewById(R.id.layoutVoterDoc)   // ✅ NEW
        tvVoterDocName = view.findViewById(R.id.tvVoterDocName)   // ✅ NEW

        etOtherEducation = EditText(requireContext())
        etOtherEducation.hint = "Enter Qualification"
        etOtherEducation.visibility = View.GONE
        etOtherEducation.setBackgroundResource(R.drawable.edit_bg)
        etOtherEducation.setPadding(30, 20, 30, 20)
        (spEducation.parent as LinearLayout).addView(etOtherEducation)

        imagePreview = ImageView(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        imagePreview.layoutParams = params
        imagePreview.scaleType = ImageView.ScaleType.CENTER_CROP
        imagePreview.visibility = View.GONE
        layoutCapture.addView(imagePreview)

        prefs = requireActivity().getSharedPreferences("BoothifyPrefs", Context.MODE_PRIVATE)
    }

    // ✅ NEW — Voter Document Picker Setup
    private fun setupVoterDocPicker() {
        layoutVoterDoc.setOnClickListener {
            showVoterDocPickerDialog()
        }
    }

    // ✅ NEW — Dialog: Camera / Gallery / PDF
    private fun showVoterDocPickerDialog() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Upload Voter ID Document")
            .setItems(arrayOf("📷  Camera", "🖼  Gallery (Image)", "📄  PDF File")) { _, which ->
                when (which) {
                    0 -> startActivityForResult(
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE), VOTER_DOC_REQUEST
                    )
                    1 -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intent.putExtra("voterDoc", true)
                        startActivityForResult(intent, VOTER_DOC_REQUEST)
                    }
                    2 -> {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "application/pdf"
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        startActivityForResult(intent, VOTER_DOC_REQUEST)
                    }
                }
            }
            .show()
    }

    // Education spinner — unchanged
    private fun setupEducationSpinner() {
        val educationList = arrayListOf(
            "Select Qualification", "10th", "12th",
            "Graduate", "PG", "Any Other"
        )
        val adapter = object : ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, educationList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val text = v.findViewById<TextView>(android.R.id.text1)
                text.setTextColor(android.graphics.Color.BLACK)
                text.textSize = 14f
                text.setPadding(8, 0, 8, 0)
                return v
            }
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val text = v.findViewById<TextView>(android.R.id.text1)
                text.setTextColor(android.graphics.Color.BLACK)
                text.setBackgroundColor(android.graphics.Color.WHITE)
                text.textSize = 14f
                text.setPadding(24, 16, 24, 16)
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEducation.adapter = adapter
        spEducation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(android.graphics.Color.BLACK)
                selectedEducation = educationList[position]
                if (selectedEducation == "Any Other") showQualificationDialog()
                else etOtherEducation.visibility = View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showQualificationDialog() {
        val ctx = requireContext()
        val root = LinearLayout(ctx)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(android.graphics.Color.WHITE)

        val header = LinearLayout(ctx)
        header.orientation = LinearLayout.VERTICAL
        header.gravity = android.view.Gravity.CENTER
        header.setBackgroundColor(android.graphics.Color.parseColor("#1E3A5F"))
        header.setPadding(40, 50, 40, 50)

        val icon = TextView(ctx); icon.text = "🎓"; icon.textSize = 36f; icon.gravity = android.view.Gravity.CENTER
        val title = TextView(ctx); title.text = "Enter Qualification"
        title.setTextColor(android.graphics.Color.parseColor("#FFD700"))
        title.textSize = 18f; title.typeface = android.graphics.Typeface.DEFAULT_BOLD
        title.gravity = android.view.Gravity.CENTER
        val titleParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        titleParams.topMargin = 12; title.layoutParams = titleParams

        val subtitle = TextView(ctx); subtitle.text = "Type your custom qualification below"
        subtitle.setTextColor(android.graphics.Color.parseColor("#94A3B8")); subtitle.textSize = 12f
        subtitle.gravity = android.view.Gravity.CENTER
        val subParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        subParams.topMargin = 6; subtitle.layoutParams = subParams

        header.addView(icon); header.addView(title, titleParams); header.addView(subtitle, subParams)

        val divider = android.view.View(ctx)
        divider.setBackgroundColor(android.graphics.Color.parseColor("#FFD700"))
        divider.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4)

        val body = LinearLayout(ctx); body.orientation = LinearLayout.VERTICAL; body.setPadding(60, 50, 60, 30)
        val inputLabel = TextView(ctx); inputLabel.text = "QUALIFICATION"; inputLabel.textSize = 10f
        inputLabel.setTextColor(android.graphics.Color.parseColor("#9CA3AF"))
        inputLabel.typeface = android.graphics.Typeface.DEFAULT_BOLD; inputLabel.letterSpacing = 0.1f

        val editText = EditText(ctx); editText.hint = "e.g. B.Tech, MBA, PhD..."
        editText.setHintTextColor(android.graphics.Color.parseColor("#9CA3AF"))
        editText.setTextColor(android.graphics.Color.parseColor("#1F2937")); editText.textSize = 15f
        editText.typeface = android.graphics.Typeface.DEFAULT_BOLD; editText.setPadding(30, 30, 30, 30)
        editText.setBackgroundResource(android.R.drawable.edit_text)
        val editParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        editParams.topMargin = 12; body.addView(inputLabel); body.addView(editText, editParams)

        val btnRow = LinearLayout(ctx); btnRow.orientation = LinearLayout.HORIZONTAL
        btnRow.gravity = android.view.Gravity.END; btnRow.setPadding(40, 20, 40, 40)
        val btnCancel = Button(ctx); btnCancel.text = "Cancel"
        btnCancel.setTextColor(android.graphics.Color.parseColor("#6B7280"))
        btnCancel.isAllCaps = false; btnCancel.background = null; btnCancel.textSize = 14f
        btnCancel.typeface = android.graphics.Typeface.DEFAULT_BOLD
        val btnSave = Button(ctx); btnSave.text = "Save"; btnSave.setTextColor(android.graphics.Color.WHITE)
        btnSave.isAllCaps = false; btnSave.textSize = 14f; btnSave.typeface = android.graphics.Typeface.DEFAULT_BOLD
        val saveBg = android.graphics.drawable.GradientDrawable()
        saveBg.shape = android.graphics.drawable.GradientDrawable.RECTANGLE; saveBg.cornerRadius = 20f
        saveBg.setColor(android.graphics.Color.parseColor("#1E3A5F")); btnSave.background = saveBg
        val saveParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        btnSave.setPadding(60, 16, 60, 16); btnRow.addView(btnCancel); btnRow.addView(btnSave, saveParams)

        root.addView(header); root.addView(divider); root.addView(body); root.addView(btnRow)

        val dialog = android.app.AlertDialog.Builder(ctx).setView(root).create()
        dialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window?.setLayout((ctx.resources.displayMetrics.widthPixels * 0.92).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        btnCancel.setOnClickListener { spEducation.setSelection(0); selectedEducation = ""; dialog.dismiss() }
        btnSave.setOnClickListener {
            val input = editText.text.toString().trim()
            if (input.isEmpty()) editText.error = "Please enter qualification"
            else { selectedEducation = input; Toast.makeText(ctx, "Qualification: $input", Toast.LENGTH_SHORT).show(); dialog.dismiss() }
        }
        dialog.show()
    }

    private fun setupImagePicker() {
        layoutCapture.setOnClickListener { showImagePickerDialog() }
    }

    private fun showImagePickerDialog() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Choose Image")
            .setItems(arrayOf("Camera", "Gallery")) { _, which ->
                if (which == 0) startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST)
                else startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GALLERY_REQUEST)
            }.show()
    }

    // ✅ UPDATED onActivityResult — handles VOTER_DOC_REQUEST too
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        try {
            when (requestCode) {

                // ── Profile image (camera) ──────────────────────────
                CAMERA_REQUEST -> {
                    val raw = data?.extras?.get("data") as? Bitmap
                    raw?.let { handleProfileImage(cropToSquare(it)) }
                }

                // ── Profile image (gallery) ─────────────────────────
                GALLERY_REQUEST -> {
                    val uri = data?.data ?: return
                    val stream = requireActivity().contentResolver.openInputStream(uri)
                    val raw = BitmapFactory.decodeStream(stream)
                    stream?.close()
                    raw?.let { handleProfileImage(cropToSquare(it)) }
                }

                // ✅ NEW — Voter Document ────────────────────────────
                VOTER_DOC_REQUEST -> {
                    val uri = data?.data

                    // Camera thumbnail (no URI)
                    if (uri == null) {
                        val bitmap = data?.extras?.get("data") as? Bitmap
                        if (bitmap != null) {
                            savedVoterDocPath = saveVoterImageToStorage(bitmap)
                            updateVoterDocUI("voter_photo.jpg")
                        }
                        return
                    }

                    val mimeType = requireActivity().contentResolver.getType(uri) ?: ""

                    if (mimeType == "application/pdf") {
                        // PDF — copy to internal storage
                        savedVoterDocPath = saveVoterPdfToStorage(uri)
                        val fileName = getFileNameFromUri(uri) ?: "voter_document.pdf"
                        updateVoterDocUI(fileName)
                    } else {
                        // Image from gallery
                        val stream = requireActivity().contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(stream)
                        stream?.close()
                        if (bitmap != null) {
                            savedVoterDocPath = saveVoterImageToStorage(bitmap)
                            updateVoterDocUI("voter_photo.jpg")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateVoterDocUI(fileName: String) {
        tvVoterDocName.text = "✓  $fileName"
        tvVoterDocName.setTextColor(android.graphics.Color.parseColor("#22C55E"))
        Toast.makeText(requireContext(), "Document uploaded: $fileName", Toast.LENGTH_SHORT).show()
    }

    // ✅ NEW — Save voter document image
    private fun saveVoterImageToStorage(bitmap: Bitmap): String {
        val filename = "voter_doc_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, filename)
        FileOutputStream(file).use { it.apply { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, this); flush() } }
        return file.absolutePath
    }

    // ✅ NEW — Save voter PDF to internal storage
    private fun saveVoterPdfToStorage(uri: Uri): String {
        val filename = "voter_doc_${System.currentTimeMillis()}.pdf"
        val file = File(requireContext().filesDir, filename)
        val inputStream: InputStream = requireActivity().contentResolver.openInputStream(uri)!!
        FileOutputStream(file).use { out -> inputStream.copyTo(out) }
        inputStream.close()
        return file.absolutePath
    }

    // ✅ NEW — Get display name from URI
    private fun getFileNameFromUri(uri: Uri): String? {
        var name: String? = null
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) name = it.getString(idx)
            }
        }
        return name
    }

    private fun handleProfileImage(bitmap: Bitmap) {
        val finalBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true)
        savedImagePath = saveImageToStorage(finalBitmap)
        imageByteArray = bitmapToByteArray(finalBitmap)
        setImagePreview(finalBitmap)
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val w = bitmap.width; val h = bitmap.height; val size = minOf(w, h)
        return Bitmap.createBitmap(bitmap, (w - size) / 2, (h - size) / 2, size, size)
    }

    private fun setImagePreview(bitmap: Bitmap) {
        layoutCapture.removeAllViews()
        val imgView = ImageView(requireContext())
        imgView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        imgView.scaleType = ImageView.ScaleType.CENTER_CROP
        imgView.setImageBitmap(bitmap)
        imgView.setOnClickListener { showImagePickerDialog() }
        layoutCapture.addView(imgView)
        imagePreview = imgView
    }

    private fun saveImageToStorage(bitmap: Bitmap): String {
        val filename = "member_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, filename)
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it); it.flush() }
        return file.absolutePath
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    private fun generateMemberId(): String {
        val db = AppDatabase.getInstance(requireContext())
        var memberId: String
        do { memberId = (1000..9999).random().toString() }
        while (db.memberDao().checkMemberId(memberId) > 0)
        return memberId
    }

    private fun setupButtons() {
        btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        btnSubmit.setOnClickListener { saveMember() }
    }

    private fun saveMember() {
        val voter = etVoter.text.toString().trim()
        val occupation = etOccupation.text.toString().trim()

        if (voter.isEmpty()) { etVoter.error = "Enter voter ID"; return }
        if (occupation.isEmpty()) { etOccupation.error = "Enter occupation"; return }
        if (selectedEducation == "Select Qualification" || selectedEducation.isEmpty()) {
            Toast.makeText(requireContext(), "Select qualification", Toast.LENGTH_SHORT).show(); return
        }
        if (savedImagePath.isEmpty()) {
            Toast.makeText(requireContext(), "Please select profile image", Toast.LENGTH_SHORT).show(); return
        }
        // ✅ Voter doc is optional — remove this check if you want to make it mandatory:
        // if (savedVoterDocPath.isEmpty()) {
        //     Toast.makeText(requireContext(), "Please upload Voter ID document", Toast.LENGTH_SHORT).show(); return
        // }

        val isExisting = arguments?.getBoolean("isExistingMember", false) ?: false
        val db = AppDatabase.getInstance(requireContext())
        val member = MemberEntity()

        member.memberId = generateMemberId()
        member.createdDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        member.name       = prefs.getString("name", "")!!
        member.surname    = prefs.getString("surname", "")!!   // ✅ NEW
        member.mobile     = prefs.getString("mobile", "")!!
        member.father     = prefs.getString("father", "")!!
        member.gender     = prefs.getString("gender", "")!!
        member.dob        = prefs.getString("dob", "")!!
        member.age        = prefs.getString("age", "")!!
        member.block      = prefs.getString("block", "")!!
        member.division   = prefs.getString("division", "")!!
        member.district   = prefs.getString("district", "")!!
        member.assembly   = prefs.getString("assembly", "")!!
        member.voterId    = voter
        member.voterDocPath = savedVoterDocPath   // ✅ NEW
        member.occupation = occupation
        member.education  = selectedEducation
        member.imagePath  = savedImagePath
        member.isExistingMember = if (isExisting) 1 else 0

        db.memberDao().insert(member)

        val dialogView = layoutInflater.inflate(R.layout.success_dialog, null)
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView).setCancelable(false).create()
        dialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireContext(), DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent); requireActivity().finishAffinity()
        }
        dialog.show()
    }
}