const BASE_URL = "http://localhost:8080";
let authToken = localStorage.getItem("Authorization");
let authenticatedUser = undefined;

/* =========================================================
   ELEMENTOS
   ========================================================= */

const userProfileButton = document.getElementById("user-profile-button");

const userPopup = document.getElementById("user-popup");

const userInformation = document.querySelector(".user-information")[0];

const createMovementModal = document.getElementById("create-movement-modal");

const closeCreateMovement = document.getElementById("close-create-movement");

const cancelCreateMovement = document.getElementById("cancel-create-movement");

const createMovementForm = document.getElementById("create-movement-form");
const openCreateMovement = document.getElementById("open-create-movement");


const loadCSVModal = document.getElementById("load-csv-modal");
const closeLoadCSV = document.getElementById("close-load-csv-movement");
const loadCSVTableForm = document.getElementById("load-csv-table-form")
const openLoadCSV = document.getElementById("load-csv-file");


const openCreateMovementTable = document.getElementById(
  "open-create-movement-table",
);

const emptyCreateMovement = document.getElementById("empty-create-movement");

const category = document.getElementById("category");

const newCategoryContainer = document.getElementById("new-category-container");

/* =========================================================
   POPUP DO USUÁRIO
   ========================================================= */

userProfileButton.addEventListener("click", function (event) {
  event.stopPropagation();

  userPopup.classList.toggle("active");
});

/*
 * Fecha o popup quando clicar fora dele.
 */

document.addEventListener("click", function (event) {
  if (
    !userPopup.contains(event.target) &&
    !userProfileButton.contains(event.target)
  ) {
    userPopup.classList.remove("active");
  }
});

/* =========================================================
   MODAL DE MOVIMENTAÇÃO
   ========================================================= */

async function openMovementModal() {

    const response = await fetch("http://localhost:8080/category", {
                method: "GET",

                headers: {
                    "Content-Type": "application/json",
                    "Authorization": authToken
                }
    });

    const data = await response.json()
    const categories = data
    const selectCategory = document.getElementById("category");
    categories.forEach(c => {
        let category = document.createElement("option")
        category.innerText = c.description
        category.value = c.description

        selectCategory.appendChild(category);
    });
    

  createMovementModal.classList.add("active");
}

function openLoadCSVModal(){
  loadCSVModal.classList.add("active");
}

function closeLoadCSVModal(){
  loadCSVModal.classList.remove("active");
}

function closeMovementModal() {
  createMovementModal.classList.remove("active");
}

/* =========================================================
   ABRIR MODAL
   ========================================================= */

openCreateMovement.addEventListener("click", openMovementModal);

openLoadCSV.addEventListener("click", openLoadCSVModal);

openCreateMovementTable.addEventListener("click", openMovementModal);

emptyCreateMovement.addEventListener("click", openMovementModal);

/* =========================================================
   FECHAR MODAL
   ========================================================= */

closeCreateMovement.addEventListener("click", closeMovementModal);

cancelCreateMovement.addEventListener("click", closeMovementModal);

closeLoadCSV.addEventListener("click", closeLoadCSVModal)

/*
 * Fecha o modal quando clicar no fundo escuro.
 */

createMovementModal.addEventListener("click", function (event) {
  if (event.target === createMovementModal) {
    closeMovementModal();
  }
});

/*
 * Permite fechar o modal pressionando ESC.
 */

document.addEventListener("keydown", function (event) {
  if (event.key === "Escape") {
    closeMovementModal();

    userPopup.classList.remove("active");
  }
});

/* =========================================================
   CATEGORIA
   ========================================================= */

category.addEventListener("change", function () {
  if (category.value === "create-new") {
    newCategoryContainer.classList.remove("hidden");
    console.log(category)
  } else {
    newCategoryContainer.classList.add("hidden");
  }
});

/* =========================================================
   FORMULÁRIO
   ========================================================= */

createMovementForm.addEventListener("submit", async function (event) {
  /*
   * Impede o navegador de recarregar a página.
   */

  event.preventDefault();

  const formData = new FormData(createMovementForm);
  const newCategoryInput = document.getElementById("new-category")

  const movement = {
    value: parseFloat(formData.get("value")),
    date: formData.get("date"),
    type: formData.get("type"),
    description: formData.get("description"),
    categoryDescription: category.value === "create-new" ? newCategoryInput.value : formData.get("category")
  };
  
    const response = await fetch("http://localhost:8080/moviment", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": authToken
            },
            body: JSON.stringify(movement)

    });
     
    

    console.log(movement)
    if(!response.ok){
        console.log(response.json());
    }
    
  /*
   * Depois que o backend responder com sucesso,
   * podemos limpar o formulário e fechar o modal.
   */

  createMovementForm.reset();

  newCategoryContainer.classList.add("hidden");

  closeMovementModal();
});

/* =========================================================
   FORMULÁRIO DE CARREGAR TABELA
   ========================================================= */

loadCSVTableForm.addEventListener("submit", async function (event) {
  /*
   * Impede o navegador de recarregar a página.
   */

  event.preventDefault();

  let confirmWindow = window.confirm("Essa ação não terá volta. Deseja continuar?")

  if(confirmWindow == false) return;

  const formData = new FormData(loadCSVTableForm);
  const fileInput = document.getElementById("file");

  const table = {
    csvFile: formData.get("file"),
    bank: formData.get("banks"),
  };

  const requestData = new FormData();

  requestData.append("csvFile", table.csvFile);
  requestData.append("bank", table.bank);

  const response = await fetch("http://localhost:8080/moviment/loadcsvfile", {
    method: "POST",
    headers: {
      Authorization: authToken,
    },
    body: requestData,
  }).catch(error => console.log(error))

  /*
   * Depois que o backend responder com sucesso,
   * podemos limpar o formulário e fechar o modal.
   */

  loadCSVTableForm.reset();

  closeLoadCSVModal();
});

/* =========================================================
   AÇÕES DO USUÁRIO
   ========================================================= */

document
  .getElementById("update-account-button")
  .addEventListener("click", function () {
    /*
     * Futuramente:
     *
     * Abrir tela/modal de atualização
     * do cadastro do usuário.
     */

    console.log("Atualizar cadastro");
  });

document
  .getElementById("delete-account-button")
  .addEventListener("click", function () {
    /*
     * Futuramente isso deverá chamar
     * uma rota DELETE do backend.
     */

    const confirmation = confirm("Tem certeza que deseja apagar sua conta?");

    if (confirmation) {
      console.log("Apagar conta");
    }
  });

document.getElementById("logout-button").addEventListener("click", function () {
  /*
   * Futuramente podemos remover o JWT,
   * session ou cookie e redirecionar
   * para a página de login.
   */

  console.log("Logout");
});

async function getAuthenticatedUserInformations(){
    const userName = document.getElementById("user-name");
    const userEmail = document.getElementById("user-email");


    const response = await fetch(BASE_URL + "/user/my", {
                method: "GET",   
                headers: {
                    "Content-Type": "application/json",
                     "Authorization": authToken
                }
    });

    const {name, email} = await response.json();

    authenticatedUser = {
        name: name,
        email: email
    }

    userName.innerText = authenticatedUser.name;
    userEmail.innerText = authenticatedUser.email;
}


async function loadBalanceInformations() {
    const currentBalance = document.getElementById("current-balance")
    const totalIncome = document.getElementById("total-income") 
    const totalExpense = document.getElementById("total-expense") 

    let currentBalanceValue = 0;
    let totalIncomeValue = 0;
    let totalExpenseValue = 0 ;

    const response = await fetch(BASE_URL + "/moviment/my", {
                method: "GET",   
                headers: {
                    "Content-Type": "application/json",
                     "Authorization": authToken
                }
    });

    const data = await response.json()
    let moviments = data
    moviments.forEach(m => {
        if(m.type === "OUT"){
            totalExpenseValue += m.value
        }else{
            totalIncomeValue += m.value
        }
        currentBalanceValue = totalIncomeValue - totalExpenseValue

        let formatter = new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

        currentBalance.innerHTML = formatter.format(currentBalanceValue)
        totalExpense.innerHTML = formatter.format(totalExpenseValue)
        totalIncome.innerHTML = formatter.format(totalIncomeValue)
    });
}   

async function loadAllMovimentsFromUser(){
    const movimentTableBody = document.getElementById("moviment-table-body");
    const emptyTable = document.getElementById("empty-table");


    const response = await fetch(BASE_URL + "/moviment/my", {
                method: "GET",   
                headers: {
                    "Content-Type": "application/json",
                     "Authorization": authToken
                }
    });

    const data = await response.json()
    let moviments = data
    

    if(data != null){
        emptyTable.style = "display: none"   
    }

    moviments.forEach(m => {
        let tr = document.createElement("tr")
        tr.style = m.type === "IN" ? `
          background-color: #12914071;
          color: rgb(6, 91, 1);

        ` : `
          background-color: #dc262652;
          color: red;
        `


        tr.innerHTML = `
            <td>${parseFloat(m.value)}</td>
            <td>${m.date}</td>
            <td>${m.type === "IN" ? "Entrada" : "Saida"}</td>
            <td>${m.description}</td>
            <td>${m.categoryDescription}</td>
            
        `
        movimentTableBody.appendChild(tr);
    });
    
}

loadBalanceInformations()
loadAllMovimentsFromUser()
getAuthenticatedUserInformations()