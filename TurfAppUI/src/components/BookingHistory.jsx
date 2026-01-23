const BookingHistory = () => {
  return (
    <div className="card shadow-sm mt-4 border-0">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="fw-bold mb-0">Booking History</h5>
          <select className="form-select form-select-sm w-auto">
            <option>Last 30 days</option>
            <option>Last 3 months</option>
            <option>All time</option>
          </select>
        </div>

        <div className="list-group list-group-flush">
          <div className="list-group-item d-flex justify-content-between align-items-center">
            <span className="fw-medium">Greenfield Arena</span>
            <span className="badge bg-success px-3 py-2">Completed</span>
          </div>

          <div className="list-group-item d-flex justify-content-between align-items-center">
            <span className="fw-medium">City Sports Hub</span>
            <span className="badge bg-warning text-dark px-3 py-2">
              Upcoming
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingHistory;
