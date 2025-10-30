const socket = new WebSocket("ws://" + window.location.host + "/ws");

socket.onopen = () => document.getElementById("status").textContent = "Connected!";
socket.onmessage = e => alert("Message: " + e.data);
socket.onclose = () => document.getElementById("status").textContent = "Disconnected";

function sendMessage() {
    socket.send("Hello from client");
}
