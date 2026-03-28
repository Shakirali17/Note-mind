import { Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Dashboard from "./pages/Dashboard";
import UploadPage from "./pages/UploadPage";
import ProtectedRoute from "./routes/ProtectedRoute";
import NoteView from "./pages/NoteView";


function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/upload"
        element={
          <ProtectedRoute>
            <UploadPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/notes/:noteId"
        element={
          <ProtectedRoute>
            <NoteView />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;
