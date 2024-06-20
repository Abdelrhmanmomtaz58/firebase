package com.momtaz.tast128

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.momtaz.tast128.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore.collection("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callBack()


    }

    private fun callBack() {
        binding.Add.setOnClickListener {
            val fName =binding.farstName.text.toString()
            val lName =binding.lastName.text.toString()
            val user =User(fName,lName)
            saveUser(user)
        }
        binding.retrieval.setOnClickListener {

            retrieval()
        }
        binding.update.setOnClickListener {
            val user =   getoldData()
            val newMap =  getNewData()
            updateData(user,newMap)
        }
        binding.delete.setOnClickListener {
            val user = getoldData()
            delete(user)
        }
        binding.next.setOnClickListener {
            startActivity(Intent(this@MainActivity,MainActivity2::class.java))
        }

    }

    private fun delete(user: User) {
        db.whereEqualTo("fname",user.fname).whereEqualTo("lname",user.lname)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    if (task.result!!.documents.isNotEmpty()){
                        for (document in task.result!!.documents){
                            db.document(document.id).delete()
                        }
                        Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this,"No Matching",Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }

    }

    private fun getNewData():Map<String,Any>{
        val fName =binding.newFName.text.toString()
        val lName =binding.newLName.text.toString()
        val map = mutableMapOf<String,Any>()
        if (fName.isNotEmpty())
        {
            map["fname"]= fName
        }
        if (lName.isNotEmpty()){
            map["lname"]= lName
        }
        return map

    }

    private fun getoldData():User {
        val fname = binding.farstName.text.toString()
        val lName = binding.lastName.text.toString()
        val user=User(fname,lName)
        return user
    }

    private fun updateData(user: User,newMap:Map<String,Any>) {
        db.whereEqualTo("fname",user.fname)
        db.whereEqualTo("lname",user.lname)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    if (task.result!!.documents.isNotEmpty()){
                        for (document in task.result!!.documents){
                            db.document(document.id).set(
                                newMap, SetOptions.merge()
                            )
                        }
                        Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this,"No Matching",Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
    }

    private fun retrieval() {
        db
            .get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful)
                {
                    val sb = StringBuilder()
                    for (document in task.result!!.documents)
                    {
                        val user =  document.toObject<User>()
                        sb.append("${user?.fname.toString()} ${user?.lname.toString()}\n")
                    }
                    binding.dataRE.text = sb
                }

            }
            .addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()

            }
    }



    private fun saveUser(user: User) {
        db
            .add(user)
            .addOnSuccessListener { task->
                Toast.makeText(this,"add Succeeded "+task.id,Toast.LENGTH_LONG).show()

            }.addOnFailureListener { e->
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }

    }
}