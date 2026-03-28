import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthCard from "../components/AuthCard";
import { registerUser } from "../api/api";


function RegisterPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!username || !password) {
      setError("All fields are required");
      return;
    }

    try {
      setLoading(true);

      await registerUser({
        username,
        password,
      });

      navigate("/login");
    } catch (err) {
      if (err.response && err.response.status === 409) {
        setError("Username already exists");
      } else {
        setError("Registration failed. Try again.");
      }
    }
    finally {
      setLoading(false);
    }

  };


  return (
    <div className="min-h-screen flex flex-col items-center justify-center ">
      <AuthCard title="Register to NoteMind">
        {/* <h3 className="text-2xl font-bold text-center text-slate-800">Login To NoteMind</h3> */}
        <div className="bgwhite p-6 rounded-lg shadow-md w-full max-w-sm">
          <form onSubmit={handleSubmit}>
            <input
              className="w-full border border-slate-300 rounded px-3 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />

            <input
              className="w-full border border-slate-300 rounded px-3 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            {error && <p className="text-red-600 text-sm mb-4 text-center">{error}</p>}
            <button className="w-full bg-blue-500 text-white py-2 rounded font-semibold hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed" type="submit" disabled={loading}>
              {loading ? "Logging in..." : "Register"}
            </button>
          </form>
        </div>
      </AuthCard>
    </div>
  );
}

export default RegisterPage;
