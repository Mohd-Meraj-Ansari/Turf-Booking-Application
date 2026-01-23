const StatCard = ({ title, value, badge, borderColor }) => {
  return (
    <div
      className="card shadow-sm h-100 border-0 position-relative"
      style={{ borderLeft: `5px solid ${borderColor}` }}
    >
      <div className="card-body d-flex justify-content-between align-items-center">
        <div>
          <p className="text-muted mb-1">{title}</p>
          <h4 className="fw-bold mb-0">{value}</h4>
        </div>

        <div
          className="rounded-circle d-flex align-items-center justify-content-center"
          style={{
            width: "42px",
            height: "42px",
            backgroundColor: `${borderColor}20`,
            fontSize: "1.4rem",
          }}
        >
          {badge}
        </div>
      </div>
    </div>
  );
};

export default StatCard;
