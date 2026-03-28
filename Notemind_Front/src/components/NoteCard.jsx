function NoteCard({ note, onView }) {
    return (
        <div className="bg-white p-4 rounded shadow flex justify-between items-center">
            <div>
                <h2 className="font-semibold text-lg">{note.title}</h2>
                <p className="text-sm text-slate-500">{note.createdAt}</p>
            </div>

            <button
                onClick={() => onView(note)}
                className="text-blue-600 font-medium hover:underline">
                View
            </button>
        </div>
    );
}

export default NoteCard;
