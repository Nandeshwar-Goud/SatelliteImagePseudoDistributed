// ================== DEBUG ==================
console.log("script.js loaded");

// ================== STATE ==================
let pollingInterval = null;
let isRunning = false;
let startBtn = null;

// ================== DOM READY ==================
document.addEventListener("DOMContentLoaded", () => {

    console.log("DOM fully loaded");

    // ===== DOM ELEMENTS =====
    startBtn = document.getElementById("startBtn");

    if (!startBtn) {
        console.error("Start button not found");
        return;
    }

    // Bind click ONCE
    startBtn.addEventListener("click", () => {
        console.log("Start button clicked");
        startProcessing();
    });
});

// ================== MAIN ENTRY ==================
async function startProcessing() {

    if (isRunning) return;
    isRunning = true;

    const statusText = document.getElementById("status");
    const totalImagesEl = document.getElementById("totalImages");
    const processedImagesEl = document.getElementById("processedImages");
    const progressBar = document.getElementById("progressBar");
    const galleryGrid = document.getElementById("galleryGrid");
    const logBox = document.getElementById("logBox");
    const tileSizeInput = document.getElementById("tileSize");

    const folderInput = document.getElementById("folderUpload");
    const zipInput = document.getElementById("zipUpload");

    // ---- RESET UI ON NEW RUN ----
    galleryGrid.innerHTML = "";
    logBox.innerHTML = "";
    processedImagesEl.innerText = "0";
    progressBar.value = 0;
    progressBar.max = 1;

    console.log("Start Processing clicked");

    const folderFiles = folderInput.files ? Array.from(folderInput.files) : [];
    const zipFiles = zipInput.files ? Array.from(zipInput.files) : [];

    if (folderFiles.length === 0 && zipFiles.length === 0) {
        alert("Please select a dataset folder or ZIP");
        isRunning = false;
        return;
    }

    startBtn.disabled = true;
    statusText.innerText = "Status: UPLOADING";
    log("Uploading dataset…");

    const formData = new FormData();
    folderFiles.forEach(f => formData.append("files", f, f.webkitRelativePath || f.name));
    zipFiles.forEach(f => formData.append("files", f));

    try {
        const uploadRes = await fetch("http://localhost:8080/api/upload-dataset", {
            method: "POST",
            body: formData
        });

        if (!uploadRes.ok) throw new Error(await uploadRes.text());

        const uploadResult = await uploadRes.json();
        totalImagesEl.innerText = uploadResult.totalImages;
        progressBar.max = uploadResult.totalImages;

        log(`Uploaded ${uploadResult.totalImages} images`);

        const tileSize = parseInt(tileSizeInput.value) || 200;

        await fetch("http://localhost:8080/api/start-processing", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ tileSize })
        });

        log("Processing started");
        statusText.innerText = "Status: RUNNING";

        pollStatus();

    } catch (e) {
        log("ERROR: " + e.message);
        statusText.innerText = "Status: ERROR";
        startBtn.disabled = false;
        isRunning = false;
    }
}

// ================== STATUS POLLING ==================
function pollStatus() {

    if (pollingInterval) clearInterval(pollingInterval);

    pollingInterval = setInterval(async () => {
        try {
            const res = await fetch("http://localhost:8080/api/status");
            if (!res.ok) return;

            const status = await res.json();

            document.getElementById("processedImages").innerText = status.processed;
            document.getElementById("totalImages").innerText = status.total;

            const bar = document.getElementById("progressBar");
            bar.max = status.total || 1;
            bar.value = status.processed || 0;

            document.getElementById("status").innerText = "Status: " + status.state;

            if (status.state === "COMPLETED") {
                clearInterval(pollingInterval);
                log("Processing completed");
                startBtn.disabled = false;
                isRunning = false;
                loadResults();
            }

        } catch (e) {
            console.error(e);
        }
    }, 1000);
}

// ================== RESULTS ==================
async function loadResults() {
    const res = await fetch("http://localhost:8080/api/results");
    if (!res.ok) return;

    const images = await res.json();
    const grid = document.getElementById("galleryGrid");
    grid.innerHTML = "";

    images.forEach(name => {
        const img = document.createElement("img");
        img.src = `http://localhost:8080/${name}?t=${Date.now()}`;
        img.style.width = "180px";
        grid.appendChild(img);
    });

    log(`Loaded ${images.length} images`);
}

// ================== LOGGING ==================
function log(msg) {
    const box = document.getElementById("logBox");
    box.innerHTML += msg + "<br>";
    box.scrollTop = box.scrollHeight;
}
