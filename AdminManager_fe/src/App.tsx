import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';

function App() {
  return (
    <BrowserRouter>
      <nav className="bg-white shadow-md p-4">
        <div className="container mx-auto flex gap-4">
          <Link to="/" className="text-blue-500 hover:text-blue-700">
            Home
          </Link>
          <Link to="/login" className="text-blue-500 hover:text-blue-700">
            Login
          </Link>
        </div>
      </nav>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
