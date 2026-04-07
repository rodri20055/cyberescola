const API = "/api";

function setSession(data){
  localStorage.setItem("token", data.token);
  localStorage.setItem("role", data.role);
  localStorage.setItem("nome", data.nome);
}

function getRole(){ return localStorage.getItem("role"); }
function logout(){
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  localStorage.removeItem("nome");
  window.location.href = "/login.html";
}

async function login(email, password){
  const res = await fetch(`${API}/auth/login`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({email, password})
  });
  return await res.json();
}