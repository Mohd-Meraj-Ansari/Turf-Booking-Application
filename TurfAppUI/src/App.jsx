import { useState } from "react";
import {BrowserRouter as Router,Routes,Route} from 'react-router-dom'
import "./App.css";
import RegisterPage from "./components/RegisterPage";
import LoginPage from "./components/LoginPage";
import HomePage from "./components/Homepage";

function App() {
  return (
    <>
        <Router>
    <Routes>
      <Route path="/" element={<HomePage />} />
         <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
           
    </Routes>
  </Router>
    </>
  );
}

export default App;
