import { useNavigate, Link } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("userId");
    navigate("/login");
  };

  return (
    <nav className="bg-white shadow px-6 py-4 flex justify-between items-center">
      <h1 className="text-xl font-bold text-blue-600">
        NoteMind
      </h1>

      <div className="flex gap-6 items-center">
        <Link to="/dashboard" className="text-slate-700 hover:text-blue-600">
          Dashboard
        </Link>

        <Link to="/upload" className="text-slate-700 hover:text-blue-600">
          Upload
        </Link>

        <button
          onClick={handleLogout}
          className="text-red-600 font-medium hover:underline"
        >
          Logout
        </button>
      </div>
    </nav>
  );
}

export default Navbar;
