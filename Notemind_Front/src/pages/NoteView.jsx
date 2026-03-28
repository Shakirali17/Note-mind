import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getNoteById } from "../api/api";
import { summarizeNote } from "../api/api";


function NoteView() {
    const { noteId } = useParams();
    const [note, setNote] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [summary, setSummary] = useState("");
    const [summaryLoading, setSummaryLoading] = useState(false);
    const [summaryError, setSummaryError] = useState("");


    useEffect(() => {
        const fetchNote = async () => {
            try {
                const response = await getNoteById(noteId);
                setNote(response.data);
                setSummary(response.data.summary || "");
            } catch (err) {
                setError("Failed to load note");
            } finally {
                setLoading(false);
            }
        };

        fetchNote();
    }, [noteId]);

        const handleGenerateSummary = async () => {
            try {
                setSummaryLoading(true);
                setSummaryError("");

                const res = await summarizeNote(noteId);
                setSummary(res.data.summary);

            } catch (err) {
                setSummaryError("Failed to generate summary");
            } finally {
                setSummaryLoading(false);
            }
        };


    if (loading) {
        return <div>Loading note...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }
    return (
        <div className="min-h-screen bg-slate-100 p-6">
            <div className="bg-white p-6 rounded shadow max-w-3xl mx-auto">
                <h1 className="text-2xl font-bold mb-2">{note.title}</h1>
                <p className="text-sm text-slate-500 mb-4">{note.createdAt}</p>

                <div className="whitespace-pre-wrap text-slate-800">
                    {note.transcription}
                </div>
                <div className="mt-6">
                    <h2 className="text-lg font-semibold mb-2">Summary</h2>

                    {summary ? (
                        <p className="bg-slate-50 p-4 rounded border text-slate-800">
                            {summary}
                        </p>
                    ) : (
                        <button
                            onClick={handleGenerateSummary}
                            disabled={summaryLoading}
                            className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 disabled:opacity-50"
                        >
                            {summaryLoading ? "Generating..." : "Generate Summary"}
                        </button>
                    )}

                    {summaryError && (
                        <p className="text-red-600 mt-2">{summaryError}</p>
                    )}
                </div>

            </div>
        </div>
    );
}

export default NoteView;
