import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import { useParams, useNavigate } from "react-router-dom";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

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
    console.log(res.data);
    setBill(res.data);
  };

  if (!bill) {
    return <p className="text-center my-5">Loading bill...</p>;
  }

  const downloadPdf = () => {
    const doc = new jsPDF();

    // Title
    doc.setFontSize(16);
    doc.text("Bill Details", 105, 15, { align: "center" });

    doc.setFontSize(10);
    doc.text(`Bill ID: ${bill.id}`, 14, 25);
    doc.text(`Status: ${bill.status}`, 160, 25);

    // Booking Details
    autoTable(doc, {
      startY: 32,
      theme: "grid",
      head: [["Field", "Value"]],
      body: [
        ["Turf Name", bill.turfName],
        ["Booking Type", bill.bookingType],
        ["Start Date", bill.startDate],
        ["End Date", bill.endDate],
        ["Start Time", bill.startTime ?? "-"],
        ["End Time", bill.endTime ?? "-"],
      ],
    });

    // Amount Details
    autoTable(doc, {
      startY: doc.lastAutoTable.finalY + 8,
      theme: "grid",
      head: [["Description", "Amount (₹)"]],
      body: [
        ["Price per Hour", bill.baseAmount.toFixed(2)],
        ["Accessories Amount", bill.accessoriesAmount.toFixed(2)],
        ["Discount", `- ${bill.discountAmount.toFixed(2)}`],
        ["Total Amount", bill.totalAmount.toFixed(2)],
      ],
      styles: { halign: "right" },
      columnStyles: {
        0: { halign: "left" },
      },
    });

    // Footer
    doc.setFontSize(9);
    doc.text(
      `Generated on ${new Date().toLocaleString()}`,
      14,
      doc.internal.pageSize.height - 10,
    );

    // Download
    doc.save(`bill-${bill.id}.pdf`);
  };

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
        <button className="btn btn-primary btn-sm" onClick={downloadPdf}>
          ⬇ Download PDF
        </button>
      </div>

      {/* Bill Card */}
      <div className="card shadow-sm border-0">
        <div className="card-body p-4">
          <h5 className="fw-bold mb-3 text-center">Bill Details</h5>

          <table className="table table-borderless mb-0">
            <tbody>
              <tr>
                <td className="text-start text-muted">Turf Name</td>
                <td className="text-end fw-semibold">{bill.turfName}</td>
              </tr>

              <tr>
                <td className="text-start text-muted">Booking Type</td>
                <td className="text-end ">{bill.bookingType}</td>
              </tr>

              <tr>
                <td className="text-start text-muted">Start Date</td>
                <td className="text-end ">{bill.startDate}</td>
              </tr>

              <tr>
                <td className="text-start text-muted">End Date</td>
                <td className="text-end ">{bill.endDate}</td>
              </tr>

              <tr>
                <td className="text-start text-muted">Start Time</td>
                <td className="text-end ">
                  {bill.startTime ? bill.startTime : "-"}
                </td>
              </tr>
              <tr>
                <td className="text-start text-muted">End Time</td>
                <td className="text-end ">
                  {bill.endTime ? bill.endTime : "-"}
                </td>
              </tr>

              <tr>
                <td colSpan="2">
                  <hr />
                </td>
              </tr>

              <tr>
                <td className="text-start text-muted">Price per Hour</td>
                <td className="text-end ">₹{bill.baseAmount.toFixed(2)}</td>
              </tr>

              <tr>
                <td className="text-start text-muted">Accessories Amount</td>
                <td className="text-end ">
                  ₹{bill.accessoriesAmount.toFixed(2)}
                </td>
              </tr>

              <tr className="text-danger">
                <td className="text-start text-muted">Discount</td>
                <td className="text-end ">₹{bill.discountAmount.toFixed(2)}</td>
              </tr>

              <tr>
                <td colSpan="2">
                  <hr />
                </td>
              </tr>

              <tr className="fw-bold fs-5">
                <td className="text-start">Total</td>
                <td className="text-end  text-success">
                  ₹{bill.totalAmount.toFixed(2)}
                </td>
              </tr>

              <tr className="fs-5">
                <td className="text-start">Status</td>
                <td className="text-end">
                  <span
                    className={`badge px-3 py-2 ${
                      bill.status === "PAID" ? "bg-success" : "bg-danger"
                    }`}
                  >
                    {bill.status}
                  </span>
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
