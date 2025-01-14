package project.an.readnewsapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import project.an.readnewsapp.R;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText inputEmailSignIn, inputPassSignIn;
    TextView forgetPass, needPass, needMail, textClickSignUp;
    Button btnSignIn;
    LinearLayout btnSignInGoogle, btnSignInFacebook, changeSignUp;

    private void getControl(){
        inputEmailSignIn = findViewById(R.id.inputEmailSignIn);
        inputPassSignIn = findViewById(R.id.inputPassSignIn);
        forgetPass = findViewById(R.id.forgetPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignInFacebook = findViewById(R.id.btnSignInFacebook);
        btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        needMail = findViewById(R.id.needMail);
        needPass = findViewById(R.id.needPass);
        inputPassSignIn = findViewById(R.id.inputPassSignIn);
        textClickSignUp = findViewById(R.id.textClickSignUp);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        getControl();
        btnSignIn.setOnClickListener(signInMailAndPass);
        inputEmailSignIn.addTextChangedListener(putEmail);
        inputPassSignIn.addTextChangedListener(putPass);
        textClickSignUp.setOnClickListener(changeSignUpActivity);
    }

    TextWatcher putEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            needMail.setText("");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher putPass = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            needPass.setText("");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnClickListener signInMailAndPass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email, pass;
            email = String.valueOf(inputEmailSignIn.getText());
            pass = String.valueOf(inputPassSignIn.getText());
            if(TextUtils.isEmpty(email)){
                needMail.setText("Hãy nhập email!");
                return;
            }
            if(TextUtils.isEmpty(pass)){
                needMail.setText("");
                needPass.setText("Hãy nhập mật khẩu!");
                return;
            }
            showResultDialog(email, pass);
        }
    };
    private void showResultDialog(String email, String pass){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        ImageView closed = dialog.findViewById(R.id.closedDialog);
        ImageView resultIcon = dialog.findViewById(R.id.resultIcon);
        TextView resultText = dialog.findViewById(R.id.resultText);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        resultIcon.setVisibility(View.GONE);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        resultIcon.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            resultIcon.setImageResource(R.drawable.icon_check_circle);
                            resultText.setText("Đăng nhập thành công");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            resultIcon.setImageResource(R.drawable.icon_false_circle);
                            resultText.setText("Đăng nhập thất bại");
//                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // Thực hiện lệnh sau 2 giây
//                                    dialog.dismiss();
//                                }
//                            }, 2000);
                        }
                    }
                });
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
    View.OnClickListener changeSignUpActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }
    };
}