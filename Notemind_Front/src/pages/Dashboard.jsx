import { useEffect, useState } from "react";
import NoteCard from "../components/NoteCard";
import { useNavigate } from "react-router-dom";
import { getUserNotes } from "../api/api";

function Dashboard() {
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [search, setSearch] = useState("");


  const handleViewNote = (note) => {
    navigate(`/notes/${note.id}`);
  };

  useEffect(() => {
    const fetchNotes = async () => {
      try {
        const userId = localStorage.getItem("userId");
        const response = await getUserNotes(userId);
        
        setNotes(response.data);
      } catch (err) {
        console.error(err);
        setError("Failed to load notes");
      } finally {
        setLoading(false);
      }
    };

    fetchNotes();
  }, []);

  if (loading) {
    return <div>Loading notes...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  const filteredNotes = notes.filter((note) =>
    note.title.toLowerCase().includes(search.toLowerCase())
  );


  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <h1 className="text-2xl font-bold mb-6">Your Notes</h1>
      <input
        type="text"
        placeholder="Search notes by title..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="w-full max-w-md mb-6 px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
      />

      {filteredNotes.length === 0 ? (
        <p className="text-slate-500">No matching notes found</p>
      ) : (
        <div className="grid gap-4">
          {filteredNotes.map((note) => (
            <NoteCard
              key={note.id}
              note={note}
              onView={handleViewNote}
            />
          ))}
        </div>
      )}

    </div>
  );
}

export default Dashboard;
