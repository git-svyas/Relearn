package com.example.mobileapp


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity.LEFT
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.adapter.SubjectsAdapter
import com.example.mobileapp.model.Subject
import com.example.myapplication.Login
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class SubjectDashboard : AppCompatActivity(), SubjectsAdapter.OnItemClickListener {

    private var recyclerView: RecyclerView? = null
    private var mSubjectList: ArrayList<Subject>? = null
    private var subjectAdapter: SubjectsAdapter? = null
    private lateinit var sharedPref: SharedPreferences
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var hMenu: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var linearLayout: LinearLayout

    lateinit var toggle : ActionBarDrawerToggle


    var mBranch:String ? = null
    var mName:String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_subjects)
        sharedPref = getSharedPreferences(getString(R.string.user_details_sf), MODE_PRIVATE)

        getPersonalData()
        Log.d("abc",mName.toString())
        Log.d("abc",mBranch.toString())


        // Toggle
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        recyclerView = findViewById(R.id.recycler_view_item)
        hMenu = findViewById(R.id.hmenu)
        toolbar = findViewById(R.id.toolbar_sub)
        linearLayout = findViewById(R.id.parent_layout_sub)

        recyclerView?.layoutManager = GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        //recyclerView?.setHasFixedSize(true)

        initRecyclerAdapter()


        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        val hview = navView.getHeaderView(0)
        val textView = hview.findViewById<TextView>(R.id.student_name_dashboard)
        textView.text = mName

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var state: Int? = null
        recyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                state = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0 && (state == 0 || state == 2)){
                    hide()
                    Log.d("Main","Hide")
                }
                else if(dy < -10){
                    show()
                    Log.d("Main","Show")
                }
            }
        })

        hMenu.setOnClickListener(View.OnClickListener {
            drawerLayout.open()
            toolbar.visibility = View.GONE
        })

        drawerLayout.setDrawerListener(object: DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                toolbar.visibility = View.VISIBLE
            }

            override fun onDrawerOpened(drawerView: View) {
                toolbar.visibility = View.GONE
            }
        })

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.sub1 -> {
                    mBranch = "CSE"
                    initRecyclerAdapter()
                    drawerLayout.closeDrawer(LEFT)
                }
                R.id.sub2 -> {
                    Snackbar.make(drawerLayout,"Currently not available",Snackbar.LENGTH_LONG).show()
                    //mBranch = "IT"
                    //initRecyclerAdapter()
                    drawerLayout.closeDrawer(LEFT)
                }
                R.id.sub3 -> {
                    Snackbar.make(drawerLayout,"Currently not available",Snackbar.LENGTH_LONG).show()
                    //mBranch = "ECE"
                    //initRecyclerAdapter()
                    drawerLayout.closeDrawer(LEFT)
                }
                R.id.abt_us -> {
                    Log.d("Main","About Us Clicked")
                    startActivity(Intent(this,AboutUs::class.java))
                }
                R.id.log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this,Login::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(LEFT)
            true
        }

    }

    private fun hide() {
        toolbar.visibility = View.GONE
    }
    private fun show() {
        toolbar.visibility = View.VISIBLE
    }

    private fun getPersonalData() {
        mName = sharedPref.getString("STUDENT_NAME",null)
        mBranch = sharedPref.getString("STUDENT_BRANCH",null)

        Log.d("Main","Branch is ${mBranch}")
        Log.d("Main","Name is ${mName}")
    }

    fun initRecyclerAdapter(){
        mSubjectList = setSubject() // return list based upon the branch
        if(mSubjectList == null){
            // handle the error
            Log.d("Main","Subject List is empty")
            return
        }
        subjectAdapter = SubjectsAdapter(applicationContext, mSubjectList!!,this)
        recyclerView?.adapter = subjectAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }


    private fun setSubject(): ArrayList<Subject> {
        var currentSubjectList: ArrayList<Subject> = ArrayList()

        val cseBranchList: ArrayList<Subject> = ArrayList()
        val IT: ArrayList<Subject> = ArrayList()
        val eceBranchList: ArrayList<Subject> = ArrayList()


        cseBranchList.add(Subject(R.drawable.ic_cn_cse, "Computer Networks","CN"))
        cseBranchList.add(Subject(R.drawable.ic_oop_cse, "Object Oriented Programming","OOP"))
        cseBranchList.add(Subject(R.drawable.ic_flat_cse, "Theory of Computation - FLAT","FLAT"))
        cseBranchList.add(Subject(R.drawable.ic_dm_cse, "Discrete Mathematics","DMGT"))
        cseBranchList.add(Subject(R.drawable.ic_spdd_cse, "Computer Architecture - SPDD","SPDD"))
        cseBranchList.add(Subject(R.drawable.ic_os_cse, "Operating System","OS"))
        cseBranchList.add(Subject(R.drawable.ic_daa_cse, "Design & Analysis of Algorithms","DAA"))
        cseBranchList.add(Subject(R.drawable.ic_es_cse, "Environmental Sciences","ES"))

        cseBranchList.add(Subject(R.drawable.ic_compiler_design_cse, "Compiler Design","CD"))
        cseBranchList.add(Subject(R.drawable.ic_software_engineering_cse, "Software Engineering","SE"))
        cseBranchList.add(Subject(R.drawable.ic_java_cse, "Java Programming","JAVA"))
        cseBranchList.add(Subject(R.drawable.ic_python_cse, "Systems Lab Python","SL"))
        cseBranchList.add(Subject(R.drawable.ic_dbms_cse, "Database Management System","DBMS"))


        /*IT.add(SubjectName(R.drawable.android, "IT251"))
        IT.add(SubjectName(R.drawable.android, "IT252"))
        IT.add(SubjectName(R.drawable.android, "IT253"))
        IT.add(SubjectName(R.drawable.android, "IT254"))


        eceBranchList.add(SubjectName(R.drawable.android, "ECE251"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE252"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE253"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))
        eceBranchList.add(SubjectName(R.drawable.android, "ECE254"))*/

        // check for the branches
        if(mBranch.equals("CSE"))
            currentSubjectList = cseBranchList
        else if(mBranch.equals("IT"))
            currentSubjectList = IT
        else if(mBranch.equals("ECE"))
            currentSubjectList = eceBranchList

        return currentSubjectList
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerVisible(LEFT)){
            drawerLayout.close()
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onClick(position: Int) {
        if(position > 7){
            Snackbar.make(recyclerView!!,"Currently Data not available",Snackbar.LENGTH_SHORT).show()
            return
        }
        val subjectNameClicked = mSubjectList?.get(position)?.code.toString()
        Log.d("Main","Clicked item is "+ subjectNameClicked)
        val intent = Intent(this,NotesDashboard::class.java)
        intent.putExtra("EXTRA_SUBJECT",subjectNameClicked)
        startActivity(intent)
    }
}