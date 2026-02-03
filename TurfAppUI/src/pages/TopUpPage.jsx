import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const TopUpPage = () => {
  const [amount, setAmount] = useState("");
  const [balance, setBalance] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const { auth } = useAuth();
  console.log("AUTH CONTEXT:", auth);

  useEffect(() => {
    const fetchBalance = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8086/api/wallet/balance",
          {
            auth: {
              username: auth.email,
              password: auth.password,
            },
          },
        );

        setBalance(res.data.balance);
      } catch (err) {
        console.error("FETCH BALANCE ERROR:", err);
        setError("Failed to load wallet balance");
      }
    };

    if (auth) {
      fetchBalance();
    }
  }, [auth]);

  const handleTopUp = async () => {
    setMessage("");
    setError("");

    if (!amount || amount <= 0) {
      setError("Enter a valid amount");
      return;
    }

    try {
      setLoading(true);

      const res = await axios.post(
        "http://localhost:8086/api/wallet/add-balance",
        null,
        {
          params: { amount },
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );

      setBalance(res.data.balance);
      setMessage(res.data.message);
      setAmount("");
    } catch (err) {
      console.error("TOP UP ERROR:", err);
      setError(err.response?.data?.message || "Top up failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="wallet-page">
      {/* balance card */}
      <div className="balance-card">
        <span className="balance-label">Wallet Balance</span>
        <h1>₹ {balance === null ? "Loading..." : balance.toFixed(2)}</h1>
      </div>

      {/* top up card */}
      <div className="topup-card">
        <h3>Top Up Wallet</h3>

        <label>Amount</label>
        <input
          type="number"
          placeholder="Enter amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />

        <div className="quick-amounts">
          {[100, 500, 1000].map((amt) => (
            <button
              key={amt}
              className={amount == amt ? "active" : ""}
              onClick={() => setAmount(amt)}
            >
              ₹{amt}
            </button>
          ))}
        </div>

        <button className="topup-btn" onClick={handleTopUp} disabled={loading}>
          {loading ? "Processing..." : "Top Up Wallet"}
        </button>

        {message && <p className="success-text">{message}</p>}
        {error && <p className="error-text">{error}</p>}
      </div>
    </div>
  );
};

export default TopUpPage;
