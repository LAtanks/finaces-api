const BASE_URL = ""

/* =========================================================
   ELEMENTOS
   ========================================================= */

const loginContainer =
    document.getElementById("login-form-container");

const registerContainer =
    document.getElementById("register-form-container");


const showRegister =
    document.getElementById("show-register");

const showLogin =
    document.getElementById("show-login");


const loginForm =
    document.getElementById("login-form");

const registerForm =
    document.getElementById("register-form");


/* =========================================================
   TROCAR PARA REGISTRO
   ========================================================= */

showRegister.addEventListener(
    "click",
    function () {

        loginContainer.classList.remove("active");

        registerContainer.classList.add("active");

    }
);


/* =========================================================
   TROCAR PARA LOGIN
   ========================================================= */

showLogin.addEventListener(
    "click",
    function () {

        registerContainer.classList.remove("active");

        loginContainer.classList.add("active");

    }
);


/* =========================================================
   MOSTRAR / OCULTAR SENHA
   ========================================================= */

const passwordButtons =
    document.querySelectorAll(".show-password");


passwordButtons.forEach(function (button) {

    button.addEventListener(
        "click",
        function () {

            const targetId =
                button.dataset.target;

            const passwordInput =
                document.getElementById(targetId);


            if (passwordInput.type === "password") {

                passwordInput.type = "text";

                button.textContent = "◉";

            } else {

                passwordInput.type = "password";

                button.textContent = "◉";

            }

        }
    );

});


/* =========================================================
   MENSAGEM
   ========================================================= */

function showMessage(
    element,
    message,
    type
) {

    element.textContent = message;

    element.className =
        `form-message ${type}`;

}


/* =========================================================
   LOGIN
   ========================================================= */

loginForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        const email =
            document.getElementById(
                "login-email"
            ).value;


        const password =
            document.getElementById(
                "login-password"
            ).value;

            
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                },

                body: JSON.stringify({
                    email,
                    password,
                }),
        });

        if(!response.ok){
            const { error } = await response.json()

            alert(error)
            showMessage(
                document.getElementById(
                    "login-message"
                ),
                error,
                "error"
            );
        }else{
            const { token } = await response.json();

            window.localStorage.setItem("Authorization", "Bearer " + token);

            console.log('Token: Bearer ' + localStorage.getItem('Authorization'));
            window.setTimeout(() => {
                window.location = "./home.html";
            }, 2000);
        }

        

        console.log("Login:", {
            email,
            password
        });
    }
);


/* =========================================================
   REGISTRO
   ========================================================= */

registerForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const name =
            document.getElementById(
                "register-name"
            ).value;


        const email =
            document.getElementById(
                "register-email"
            ).value;


        const password =
            document.getElementById(
                "register-password"
            ).value;


        const confirmPassword =
            document.getElementById(
                "confirm-password"
            ).value;
            

        /*
         * Validação simples no frontend.
         *
         * A validação definitiva também deve
         * existir no backend.
         */

        if (password !== confirmPassword) {

            showMessage(
                document.getElementById(
                    "register-message"
                ),
                "As senhas não são iguais.",
                "error"
            );

            return;

        }

        const response = await fetch("http://localhost:8080/auth/register", {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                },

                body: JSON.stringify({
                    name,
                    password,
                    email,
                    movimentsNumber: 0
                }),
        });

        if(!response.ok){
            const {message} = await response.json()

            console.log(message)
            alert(message)
            showMessage(
                document.getElementById(
                    "register-message"
                ),
                message,
                "error"
            );
        }


        console.log("Registro:", {
            name,
            email,
            password
        });
    }
);


/* =========================================================
   ESQUECI MINHA SENHA
   ========================================================= */

document
    .getElementById("forgot-password")
    .addEventListener(
        "click",
        function (event) {

            event.preventDefault();

            alert(
                "A recuperação de senha será implementada posteriormente."
            );

        }
    );

