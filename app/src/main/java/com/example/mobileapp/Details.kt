package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mobileapp.adapter.BranchAdapter
import com.example.mobileapp.model.Branch
import com.google.android.material.snackbar.Snackbar


class Details : AppCompatActivity(){

    private lateinit var branchSpinner: Spinner
    private lateinit var branchAdapter: BranchAdapter
    private lateinit var constraintLayout: ConstraintLayout

    private lateinit var semSpinner: Spinner
    private lateinit var nameEditView: EditText
    private var branchList: ArrayList<Branch> = ArrayList()
    private var branchMap: HashMap<Int,String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)


        branchSpinner =  findViewById(R.id.branch)
        semSpinner = findViewById(R.id.sem)
        nameEditView = findViewById(R.id.name)
        constraintLayout = findViewById(R.id.cl_details)
        val proceedButton = findViewById<Button>(R.id.nxtBtn)
        initList()

        branchAdapter = BranchAdapter(this,branchList)
        branchSpinner.adapter = branchAdapter

        constraintLayout.setOnClickListener(View.OnClickListener {
            hideKeyboard()
        })
        proceedButton.setOnClickListener{
            val branch = branchSpinner.selectedItem
            val name = nameEditView.text
            if(nameEditView.text.isEmpty()){
                Log.d("Main","Name is empty")
                nameEditView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))

            }
            else if(branch == 1 || branch == 2){
                Log.d("Main","Data for only CSE branch is avaiable at this moment")
                val error = "               SELECT COMPUTER SCIENCE  \n          Only CSE data is available as of now"
                branchSpinner.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))
                val sb: Snackbar = Snackbar.make(proceedButton, error, Snackbar.LENGTH_LONG)
                    .setAnchorView(proceedButton)

                val sbl: Snackbar.SnackbarLayout = sb.view as Snackbar.SnackbarLayout
                sbl.minimumHeight = 100
                sb.show()
            }
            else{
                Log.d("Main", "branch is $branch")
                getSharedPreferences(getString(R.string.user_details_sf), MODE_PRIVATE).edit()
                    .putString("STUDENT_NAME", name.toString())
                    .putString("STUDENT_BRANCH", branchMap[branch])
                    .apply()

                Log.d("Main", branchMap[branch] +" is the branch selected")
                Log.d("Main", "$name is the name selected")

                intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

//        branchSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//            }
//
//        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = getCurrentFocus()
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initList() {
        val cse = "Computer Science"
        val it = "Information Tech"
        val ece = "Electronic & Comm"
        branchMap.put(0,"CSE")
        branchMap.put(1,"IT")
        branchMap.put(2,"ECE")
        branchList.add(Branch(cse,R.drawable.ic_branch_cse))
        branchList.add(Branch(it,R.drawable.ic_branch_it))
        branchList.add(Branch(ece,R.drawable.ic_branch_ece))

    }

}