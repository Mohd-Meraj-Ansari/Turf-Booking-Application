import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import "../styles/Billspage.css";

const BillsPage = () => {
  const { auth } = useAuth();
  const navigate = useNavigate();
  const [bills, setBills] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBills();
  }, []);

  const fetchBills = async () => {
    try {
      const res = await axios.get("http://localhost:8086/api/bills", {
        auth: {
          username: auth.email,
          password: auth.password,
        },
      });
      // console.log(res.data);
      const sortedBills = [...res.data].sort((a, b) => a.id - b.id);
      setBills(sortedBills);
      // setBills(res.data);
    } catch (err) {
      alert("Failed to load bills");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p className="loading">Loading bills...</p>;

  return (
    <div className="bills-container">
      <h2 className="page-title">My Bills</h2>

      {bills.length === 0 ? (
        <p className="empty-text">No bills available</p>
      ) : (
        <table className="bills-table">
          <thead>
            <tr>
              <th>Bill ID</th>
              <th>Total Amount</th>
              <th>Status</th>
              <th>View more</th>
            </tr>
          </thead>

          <tbody>
            {bills.map((bill) => (
              <tr
                key={bill.id}
                onClick={() => navigate(`/client/bills/${bill.id}`)}
              >
                <td>{bill.id}</td>
                <td>₹{bill.totalAmount.toFixed(2)}</td>
                <td>
                  <span className="status">{bill.status}</span>
                </td>
                <td className="view-link">View →</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default BillsPage;
