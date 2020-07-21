package com.kodingnext.passwordkeeper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kodingnext.passwordkeeper.sqlite.AppController;
import com.kodingnext.passwordkeeper.sqlite.DaoAccess;
import com.kodingnext.passwordkeeper.sqlite.ModelPassword;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //initialize RecyclerView
    RecyclerView rv_listPassword;

    //initialize List Password
    List<ModelPassword> listPassword;

    //initialize DAO Access
    DaoAccess daoAccess;

    //initialize AdapterPassword
    AdapterPassword adapterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //========================================= Read Start ===================================//

        //initialize AppController with two required parameter, databaseName and Context
        daoAccess = AppController.getInstance("password_db", MainActivity.this).daoAccess();

        //select data from database and fill it to listPassword
        listPassword = daoAccess.selectAllPassword();

        //initialize RecyclerView component
        rv_listPassword = findViewById(R.id.listPassword_rv);

        //setting the type of list, LinearLayoutManager default is inform list to arange the item from top to bottom
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        //apply the recyclerView type to llm that we define before
        rv_listPassword.setLayoutManager(llm);

        //to make initialize the AdapterPassword we need to !CREATE CLASS AdapterPassword FIRST!
        //tell the MainActivity to adding the new family that named AdapterPassword
        adapterPassword = new AdapterPassword(listPassword);
        //apply changes
        rv_listPassword.setAdapter(adapterPassword);

        //========================================= Read End =====================================//


        //initialize the Text Field et_search
        EditText et_search = findViewById(R.id.search_et);

        //adding the action when user typing the text
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //adding the action to refresh the list according to text that we type in variable s.toString()
                listPassword = daoAccess.searchPassword(s.toString());
                adapterPassword.addData(listPassword);
            }
        });
    }


    //========================================= Create Start =====================================//

    public void savePassword(View view) {

        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        //initialize the textfield component
        EditText et_password_name = findViewById(R.id.password_name_et);
        EditText et_emailoruser = findViewById(R.id.emailoruser_et);
        EditText et_password = findViewById(R.id.password_et);


        //to make initialize the ModelPassword we need to !CREATE CLASS ModelPassword FIRST!
        //create the data
        ModelPassword modelPassword = new ModelPassword();
        modelPassword.setName("" + et_password_name.getText());
        modelPassword.setEmailoruser("" + et_emailoruser.getText());
        modelPassword.setPassword("" + et_password.getText());
        daoAccess.createPassword(modelPassword);

        //refresh the list
        listPassword = daoAccess.selectAllPassword();
        adapterPassword.addData(listPassword);

        //refresh the textfield
        et_password_name.setText("");
        et_emailoruser.setText("");
        et_password.setText("");
    }

    //========================================= Create End =====================================//


    //to extends RecyclerView.Adapter<AdapterPassword.MyViewHolder>, we need to !CREATE CLASS MyViewHolder FIRST!
    //class Adapter Password is for manage the list, that must extends RecyclerView.Adapter
    public class AdapterPassword extends RecyclerView.Adapter<AdapterPassword.MyViewHolder> {

        //initialize list password to accomodate password parameter in constructor from outside of adapter password
        List<ModelPassword> listPassword;
        //initialize list password to accomodate context parameter in constructor from outside of adapter password
        Context ctx;

        //create constructor for accomodate the list parameter from outside
        public AdapterPassword(List<ModelPassword> listPassword) {

            //initialize arraylist for accomodate data from database
            this.listPassword = new ArrayList<>();
            //fill the new arraylist "listPassword" in the top of AdapterPassword with data from parameter "listPassword" --> public AdapterPassword(List<ModelPassword> "listPassword") ..
            this.listPassword = listPassword;
            //Refresh changes
            notifyDataSetChanged();
        }

        public void addData(List<ModelPassword> listPassword) {
            //fill the arraylist with data from parameter "listPassword" --> public AdapterPassword(List<ModelPassword> "listPassword") ..
            this.listPassword = listPassword;
            //Refresh changes
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Set layout for each item in list
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);

            //to fill the "ctx" in the top of AdapterPassword with context that we got from parent.getContext()
            this.ctx = parent.getContext();

            //Return to new MyViewHolder
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            //to set the tv_password_name, we need to !INITIALIZE IT IN CLASS MyViewHolder FIRST!
            //get data in current position
            ModelPassword modelPassword = listPassword.get(position);
            //set the data
            holder.tv_password_name.setText(modelPassword.getName());
        }

        @Override
        public int getItemCount() {
            //manage the list item size
            return listPassword.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            //initialize text password name
            TextView tv_password_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                //initialize text password name
                //to set the clicklistener, we need to !IMPLEMENTS View.OnClickListener IN CLASS MyViewHolder FIRST!
                itemView.setOnClickListener(this);
                //initialize text password name
                tv_password_name = itemView.findViewById(R.id.password_name_tv);
                //to set the longclicklistener, we need to !IMPLEMENTS View.OnLongClickListener IN CLASS MyViewHolder FIRST!
                itemView.setOnLongClickListener(this);
            }

            //Set the function of click once here
            @Override
            public void onClick(View v) {
                //to implement the dialogViewPassword(), we need to !CREATE dialogViewPassword() function FIRST!
                dialogViewPassword();
            }

            //Set the function of long click here
            @Override
            public boolean onLongClick(View v) {
                //to implement the dialogPilihan(), we need to !CREATE dialogPilihan() function FIRST!
                dialogPilihan();
                return false;
            }

            //Set the function to refresh list
            void refreshList() {
                listPassword = new ArrayList<>();
                listPassword = daoAccess.selectAllPassword();
                notifyDataSetChanged();
            }

            //========================= Show Detail Password Start ===============================//
            //Set the function to showing pop up Alert Dialog for data password
            void dialogViewPassword() {
                //initialize the alert dialog
                final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(ctx).create();

                //set the pop up alert dialog layout for show data password dialog
                View dialogView = ((MainActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_password, null);

                //set View dialogView
                dialog.setView(dialogView);
                //disable from dismissing the dialog
                dialog.setCancelable(false);
                //showing the dialog
                dialog.show();

                //initialize component inside show data password dialog
                TextView tv_title = dialogView.findViewById(R.id.title_password_tv);
                TextView tv_emailoruser = dialogView.findViewById(R.id.emailoruser_view_tv);
                final TextView tv_password = dialogView.findViewById(R.id.password_view_tv);
                Button btn_copy = dialogView.findViewById(R.id.submit_edit_btn);
                Button btn_ok = dialogView.findViewById(R.id.cancel_edit_btn);

                //set data of show data password dialog from list password that we intialize before in top of adapter password position
                tv_title.setText("Password for: " + listPassword.get(getAdapterPosition()).getName());
                tv_emailoruser.setText(listPassword.get(getAdapterPosition()).getEmailoruser());
                tv_password.setText(listPassword.get(getAdapterPosition()).getPassword());

                //set action of copy button when clicked
                btn_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData cd = ClipData.newPlainText("password_keeper", tv_password.getText());
                        cm.setPrimaryClip(cd);

                        //dismiss the dialog
                        dialog.dismiss();
                        //showing  message to inform the user the password copied
                        Toast.makeText(ctx, "Password Copied!", Toast.LENGTH_LONG).show();
                    }
                });

                //set action of dismiss button when clicked
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dismiss the dialog
                        dialog.dismiss();
                    }
                });

            }
            //========================== Show Detail Password End ================================//

            //============================== Chooser Dialog Start ================================//
            //set the function to showing pop up Alert Dialog for chooser option between Edit and Delete
            void dialogPilihan() {
                //initialize the alert dialog
                final android.app.AlertDialog dialogPilihan = new android.app.AlertDialog.Builder(ctx).create();

                //set the pop up alert dialog layout for chooser option dialog
                View dialogView = ((MainActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_option, null);
                //set View dialogView
                dialogPilihan.setView(dialogView);
                //showing the dialog
                dialogPilihan.show();

                //initialize component inside chooser option dialog
                TextView tv_edit = dialogView.findViewById(R.id.edit_choose_dialog);
                TextView tv_delete = dialogView.findViewById(R.id.delete_choose_dialog);

                //set action of edit button when clicked
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //to implement the dialogEdit(), we need to !CREATE dialogEdit() function FIRST!
                        //goto edit dialog
                        dialogEdit();
                        //dismiss the dialog
                        dialogPilihan.dismiss();
                    }
                });

                //set action of delete button when clicked
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //to implement the refreshList(), we need to !CREATE refreshList() function FIRST!
                        //delete password
                        daoAccess.deletePassword(listPassword.get(getAdapterPosition()));
                        //refresh list
                        refreshList();
                        //dismiss the dialog
                        dialogPilihan.dismiss();
                    }
                });
            }
            //=============================== Chooser Dialog End =================================//

            //====================================== Edit Start ==================================//
            //set the function to showing pop up Alert Dialog for edit password data
            void dialogEdit() {
                //initialize the dialog
                final android.app.AlertDialog dialogEdit = new android.app.AlertDialog.Builder(ctx, R.style.CustomAlertDialog).create();

                //set the pop up alert dialog layout for chooser option dialog
                View dialogView = ((MainActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_edit, null);

                //set View dialogView
                dialogEdit.setView(dialogView);
                //disable from dismissing the dialog
                dialogEdit.setCancelable(false);
                //show the dialog
                dialogEdit.show();

                //initialize component inside dialog edit
                final EditText et_password_name = dialogView.findViewById(R.id.password_name_et);
                final EditText et_email_or_username = dialogView.findViewById(R.id.email_or_username_et);
                final EditText et_password = dialogView.findViewById(R.id.password_et);
                Button btn_submit = dialogView.findViewById(R.id.submit_edit_btn);
                Button btn_cancel = dialogView.findViewById(R.id.cancel_edit_btn);

                //set data of edit data password dialog from list password that we initialize before in the top of adapter password position
                et_password_name.setText(listPassword.get(getAdapterPosition()).getName());
                et_email_or_username.setText(listPassword.get(getAdapterPosition()).getEmailoruser());
                et_password.setText(listPassword.get(getAdapterPosition()).getPassword());

                //set action of submit button
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //to make initialize the ModelPassword we need to !CREATE CLASS ModelPassword FIRST!
                        //update the data
                        ModelPassword modelPassword = new ModelPassword();
                        modelPassword.setId(listPassword.get(getAdapterPosition()).getId());
                        modelPassword.setPassword(et_password.getText().toString());
                        modelPassword.setEmailoruser(et_email_or_username.getText().toString());
                        modelPassword.setName(et_password_name.getText().toString());
                        daoAccess.updatePassword(modelPassword);

                        //refresh the list
                        refreshList();
                        //dimiss the dialog
                        dialogEdit.dismiss();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dismiss the dialog
                        dialogEdit.dismiss();
                    }
                });
            }
            //======================================= Edit End ===================================//

        }

    }

    //========================================= Logout Start =====================================//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set menu logout and inflate the icon of logout button
        getMenuInflater().inflate(R.menu.menu_top_right, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //make selection with Switch Case

        switch (item.getItemId()) {
            //if the menu id equals R.id.logout_btn
            case R.id.logout_btn:
                //then show the pop up that make sure if the user want to choose logout or not

                //initialize the dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
                //disable from dismissing the dialog
                dialog.setCancelable(false);
                //set message body to logout dialog
                dialog.setMessage("Are you sure to logout?");

                //set button yes
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if clicked, then dismiss and show message dialog to inform the user that logout is success
                        dialog.dismiss();
                        //show pop up toast message logout success
                        Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();

                        //change variable IsLoggedIn to false
                        SharedPreferences mPreferences = getSharedPreferences("userData", MODE_PRIVATE);
                        final SharedPreferences.Editor edit = mPreferences.edit();
                        edit.putBoolean("IsLoggedIn", false).apply();

                        //change page to LoginActivity again
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                    }
                });

                //set button no
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Logout Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                //showing the dialog
                dialog.show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    //======================================== Logout End ====================================//

}
