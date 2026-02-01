import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/WalletTransactionPage.css";

const WalletTransactionPage = () => {
  const { auth } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [filter, setFilter] = useState("ALL");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTransactions();
  }, []);

  useEffect(() => {
    applyFilter();
  }, [filter, transactions]);

  async function fetchTransactions() {
    try {
      const res = await axios.get(
        "http://localhost:8086/api/wallet/transactions",
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );

      setTransactions(res.data);
      setFiltered(res.data);
    } catch (err) {
      alert("Failed to load wallet transactions");
    } finally {
      setLoading(false);
    }
  }

  const applyFilter = () => {
    if (filter === "ALL") {
      setFiltered(transactions);
    } else {
      setFiltered(transactions.filter((t) => t.type === filter));
    }
  };

  if (loading) return <p className="transactions-loading">Loading wallet...</p>;

  return (
    <div className="transactions-page">
      <div className="transactions-header">
        <h2 className="transactions-title">Wallet Transactions</h2>

        <select
          className="transactions-filter"
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        >
          <option value="ALL">All</option>
          <option value="CREDIT">Credit</option>
          <option value="DEBIT">Debit</option>
        </select>
      </div>

      {filtered.length === 0 ? (
        <p className="transactions-empty">No transactions found</p>
      ) : (
        <div className="transactions-card">
          <table className="transactions-table text-center">
            <thead>
              <tr>
                <th>Serial No</th>
                {/* <th>Date</th> */}
                <th>Description</th>
                <th>Type</th>
                <th>Amount (₹)</th>
                <th>Balance After</th>
              </tr>
            </thead>

            <tbody>
              {filtered.map((t, index) => (
                <tr key={t.id}>
                  <td>{index + 1}</td>
                  {/* <td>{t.date}</td> */}
                  <td>{t.description}</td>

                  <td>
                    <span
                      className={`txn-type ${
                        t.type === "CREDIT" ? "credit" : "debit"
                      }`}
                    >
                      {t.type}
                    </span>
                  </td>

                  <td
                    className={`amount ${
                      t.type === "CREDIT" ? "credit" : "debit"
                    }`}
                  >
                    {t.type === "CREDIT" ? "+" : "-"}₹{t.amount}
                  </td>

                  <td>₹{t.balanceAfter}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default WalletTransactionPage;
