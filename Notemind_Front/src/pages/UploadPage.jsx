import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import AuthCard from "../components/AuthCard";
import { uploadVoice } from "../api/api";

function UploadPage() {
  const [file, setFile] = useState(null);
  const [title, setTitle] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [transcription, setTranscription] = useState("");
  const [noteId, setNoteId] = useState(null);
  const [isRecording, setIsrecording] = useState("");
  const [mediaRecorder, setMediaRecorder] = useState("");
  const [recordedBlob, setRecordedBlob] = useState(null);
  const [audioPreviewUrl, setAudioPreviewUrl] = useState(null);


  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (!selectedFile) return;

    setFile(selectedFile);
    setRecordedBlob(null); // clear recording if file chosen

    const url = URL.createObjectURL(selectedFile);
    setAudioPreviewUrl(url);
  };


  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!file && !recordedBlob) {
      setError("Please select or record an audio file");
      return;
    }

    try {
      setLoading(true);

      const userId = localStorage.getItem("userId");

      const formData = new FormData();
      if (file) {
        formData.append("file", file);
      } else if (recordedBlob) {
        formData.append("file", recordedBlob, "recorded-audio.webm");
      }
      formData.append("userId", userId);
      formData.append("title", title);

      const response = await uploadVoice(formData);

      setSuccess("Audio uploaded and transcribed successfully!");
      setTranscription(response.data.transcription);
      setNoteId(response.data.noteId);

      setTimeout(() => {
        setSuccess("");
      }, 3000);

      setFile(null);
      setRecordedBlob(null);
      setTitle("");
      setAudioPreviewUrl(null);

      // optional redirect later
      // navigate("/dashboard");

    } catch (err) {
      setError("Upload failed");
    } finally {
      setLoading(false);
    }
  };

  const startRecording = async () => {
    setError("");
    setSuccess("");

    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });

      const recorder = new MediaRecorder(stream);
      setMediaRecorder(recorder);

      const chunks = [];
      recorder.ondataavailable = (e) => {
        if (e.data.size > 0) chunks.push(e.data);
      };

      recorder.onstop = () => {
        const blob = new Blob(chunks, { type: "audio/webm" });
        setRecordedBlob(blob);
        setFile(null);
        const url = URL.createObjectURL(blob);
        setAudioPreviewUrl(url);
      };


      recorder.start();
      setIsrecording(true);
    } catch (err) {
      setError("Microphone access denied");
    }
  };

  const stopRecording = () => {
    if (mediaRecorder) {
      mediaRecorder.stop();
      setIsrecording(false);
    }
  };



  useEffect(() => {
    if (noteId) {
      const timer = setTimeout(() => {
        navigate(`/notes/${noteId}`);
      }, 2000);

      return () => clearTimeout(timer);
    }
  }, [noteId, navigate]);




  return (
    <div className="min-h-screen flex flex-col items-center justify-center gap-8 bg-slate-100">
      <AuthCard title="Upload Audio Note">
        <div className="bgwhite p-6 rounded-lg shadow-md w-full max-w-sm">
          <form onSubmit={handleSubmit}>
            {success && (
              <p className="text-green-600 text-sm text-center mb-4">
                {success}
              </p>
            )}

            <input
              className="w-full border border-slate-300 rounded px-3 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="title"
              placeholder="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />

            <div className="flex items-stretch gap-3 mb-4">
              {/* File selector */}
              <label className="flex-1">
                <input
                  type="file"
                  accept="audio/*"
                  onChange={handleFileChange}
                  className="w-full h-full border border-slate-300 rounded px-3 py-2 cursor-pointer"
                />
              </label>

              {/* Mic button */}
              {!isRecording ? (
                <button
                  type="button"
                  onClick={startRecording}
                  className="px-10 py-2 bg-green-500 text-white rounded hover:bg-green-600 flex items-center justify-center"
                >
                  🎙️ Start
                </button>
              ) : (
                <button
                  type="button"
                  onClick={stopRecording}
                  className="px-10 py-2 bg-red-500 text-white rounded hover:bg-red-600 flex items-center justify-center"
                >
                  ⏹️ Stop
                </button>
              )}
            </div>


            {audioPreviewUrl && (
              <div className="mb-4">
                {/* <p className="text-sm text-slate-600 mb-1 text-center">
                  Audio Preview
                </p> */}
                <audio
                  controls
                  src={audioPreviewUrl}
                  className="w-full"
                />
              </div>
            )}

            {error && <p className="text-red-600 text-sm mb-4 text-center">{error}</p>}
            <button className="w-full bg-blue-500 text-white mb-2 py-2 rounded font-semibold hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed" type="submit" disabled={loading}>
              {loading ? "Uploading..." : "Upload"}
            </button>

            {recordedBlob && (
              <p className="text-sm text-green-600 mb-2 text-center">
                ✅ Audio recorded and ready to upload
              </p>
            )}

            {transcription && (
              <div className="mt-4 p-3 border rounded bg-slate-50 text-sm whitespace-pre-wrap">
                <strong>Transcription:</strong>
                <p className="mt-2">{transcription}</p>
              </div>
            )}
          </form>
        </div>
      </AuthCard>
    </div>
  );
}

export default UploadPage;
