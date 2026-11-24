# Spring AI Learning Labs

Praktikum orisinal berbahasa Indonesia untuk mengeksplorasi penggunaan `ChatClient`,
structured output, dan evaluasi respons menggunakan Spring AI. Proyek ini dibuat
sebagai pendamping belajar berdasarkan tema umum Chapter 1 (getting started) dan
Chapter 2 (evaluating generated responses), bukan salinan listing atau eksperimen
dari buku.

## Lingkungan

- Java 25 LTS
- Maven 3.9+
- Spring Boot 3.5.16
- Spring AI 1.0.0

Profil `demo` aktif secara default dan memakai `DemoChatModel` lokal agar semua
praktikum dan tes dapat dijalankan tanpa akun atau API key. Model demo bersifat
deterministik dan menggunakan pencocokan kata sederhana untuk evaluasi; hasilnya
bukan tolok ukur model bahasa sungguhan.

Untuk memakai model OpenAI, atur `OPENAI_API_KEY` di environment lalu jalankan
dengan profil `openai`:

```powershell
$env:OPENAI_API_KEY = "isi-api-key-anda"
mvn spring-boot:run "-Dspring-boot.run.profiles=openai"
```

Jangan simpan API key di source control.

## Menjalankan praktikum

```powershell
mvn test
mvn spring-boot:run
```

Endpoint:

| Praktikum | Endpoint |
|---|---|
| Prompt teks sederhana | `GET /api/labs/chapter-1/basic?concept=Spring%20AI` |
| Prompt dengan structured output | `GET /api/labs/chapter-1/structured?concept=Spring%20AI` |
| Evaluasi relevansi | `POST /api/labs/chapter-2/relevancy` |
| Pemeriksaan klaim terhadap konteks | `POST /api/labs/chapter-2/fact-check` |

Contoh body evaluasi:

```json
{
  "question": "What is the boiling point of water?",
  "context": "At sea level, pure water boils at 100 degrees Celsius.",
  "response": "At sea level, pure water boils at 100 degrees Celsius."
}
```

Laporan Word praktikum ada di `docs/Spring-AI-Praktikum-Chapter-1-2.docx`. Tangkapan
layar ringkasan pengujian JUnit tersedia di `docs/screenshots/`; laporan menjelaskan
bahwa contoh dan kode merupakan implementasi orisinal untuk konsep umum, bukan salinan
listing atau eksperimen dari buku.
