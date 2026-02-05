import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import { useParams, useNavigate } from "react-router-dom";

const BillDetailsPage = () => {
  const { billId } = useParams();
  const { auth } = useAuth();
  const navigate = useNavigate();

  const [bill, setBill] = useState(null);

  useEffect(() => {
    fetchBill();
  }, []);

  const fetchBill = async () => {
    const res = await axios.get(`http://localhost:8086/api/bills/${billId}`, {
      auth: {
        username: auth.email,
        password: auth.password,
      },
    });
    setBill(res.data);
  };

  if (!bill) {
    return <p className="text-center my-5">Loading bill...</p>;
  }

  return (
    <div className="container my-4" style={{ maxWidth: "850px" }}>
      {/* Header */}
      <div className="d-flex justify-content-between align-items-center mb-3">
        <button
          className="btn btn-outline-secondary btn-sm"
          onClick={() => navigate(-1)}
        >
          ← Back
        </button>

        <span
          className={`badge px-3 py-2 ${
            bill.status === "PAID" ? "bg-success" : "bg-danger"
          }`}
        >
          {bill.status}
        </span>
      </div>

      {/* Bill Card */}
      <div className="card shadow-sm border-0">
        <div className="card-body p-4">
          <h5 className="fw-bold mb-3 text-center">Bill Details</h5>

          <table className="table table-borderless mb-0">
            <tbody>
              <tr>
                <td className="text-muted">Turf Name</td>
                <td className="text-end fw-semibold">{bill.turfName}</td>
              </tr>

              <tr>
                <td className="text-muted">Booking Type</td>
                <td className="text-end">{bill.bookingType}</td>
              </tr>

              <tr>
                <td className="text-muted">Start Date</td>
                <td className="text-end">{bill.startDate}</td>
              </tr>

              <tr>
                <td className="text-muted">End Date</td>
                <td className="text-end">{bill.endDate}</td>
              </tr>

              <tr>
                <td colSpan="2">
                  <hr />
                </td>
              </tr>

              <tr>
                <td className="text-muted">Base Amount</td>
                <td className="text-end">₹{bill.baseAmount.toFixed(2)}</td>
              </tr>

              <tr>
                <td className="text-muted">Accessories Amount</td>
                <td className="text-end">
                  ₹{bill.accessoriesAmount.toFixed(2)}
                </td>
              </tr>

              <tr className="text-danger">
                <td className="text-muted">Discount</td>
                <td className="text-end">
                  - ₹{bill.discountAmount.toFixed(2)}
                </td>
              </tr>

              <tr>
                <td colSpan="2">
                  <hr />
                </td>
              </tr>

              <tr className="fw-bold fs-5">
                <td>Total</td>
                <td className="text-end text-success">
                  ₹{bill.totalAmount.toFixed(2)}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default BillDetailsPage;
