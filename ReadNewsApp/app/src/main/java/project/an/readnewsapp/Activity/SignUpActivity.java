package project.an.readnewsapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import project.an.readnewsapp.R;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText inputEmailSignUp, inputPassSignUp, inputUserSignUp, inputConfirmPassSignUp;
    TextInputLayout inputEmailLayoutSignUp, inputPassLayoutSignUp, inputUserLayoutSignUp, inputConfirmPassLayoutSignUp;
    TextView ivalidPass, ivalidMail, ivalidUser, textClickSignIn;
    Button btnSignUp;
    String email, password, userName;

    private void getControl(){
        inputEmailSignUp = findViewById(R.id.inputEmailSignUp);
        inputPassSignUp = findViewById(R.id.inputPassSignUp);
        inputUserSignUp = findViewById(R.id.inputUserSignUp);
        inputConfirmPassSignUp = findViewById((R.id.inputConfirmPassSignUp));
        inputEmailLayoutSignUp = findViewById(R.id.inputEmailLayoutSignUp);
        inputPassLayoutSignUp = findViewById(R.id.inputPassLayoutSignUp);
        inputUserLayoutSignUp = findViewById(R.id.inputUserLayoutSignUp);
        inputConfirmPassLayoutSignUp = findViewById((R.id.inputConfirmPassLayoutSignUp));
        btnSignUp = findViewById(R.id.btnSignUp);
        ivalidMail = findViewById(R.id.ivalidMail);
        ivalidPass = findViewById(R.id.ivalidPass);
        ivalidUser = findViewById(R.id.ivalidUser);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        getControl();
        inputEmailSignUp.addTextChangedListener(checkEmail);
        inputPassSignUp.addTextChangedListener(checkPass);
    }

    //Check mật khẩu: ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }

    //check mail
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailPattern);
    }

    //check tên tài khoản
    private boolean isValidUserName(String userName) {
        // Biểu thức chính quy để kiểm tra userName
        String userNamePattern = "^[a-zA-Z][a-zA-Z0-9_]*$";
        return userName.matches(userNamePattern);
    }

    TextWatcher checkEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            email = inputEmailSignUp.getText().toString();
            if(!isValidEmail(email)){
                ivalidMail.setText("Email chưa hợp lệ!");
            }
            else{
                ivalidMail.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s){
        }
    };

    TextWatcher checkPass = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            password = inputPassSignUp.getText().toString();
            if(!isValidPassword(password)){
                ivalidPass.setText("Mật khẩu phải từ 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
            }
            else{
                ivalidPass.setText("");

            }
        }

        @Override
        public void afterTextChanged(Editable s){
        }
    };

    TextWatcher checkUser = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userName = inputUserSignUp.getText().toString();
            if(!isValidPassword(userName)){
                ivalidUser.setText("Mật khẩu phải từ 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
            }
            else{
                ivalidUser.setText("");

            }
        }

        @Override
        public void afterTextChanged(Editable s){
        }
    };

    View.OnClickListener changeSignInActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signInIntent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(signInIntent);
        }
    };
}