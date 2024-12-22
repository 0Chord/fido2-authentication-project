import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/LoginPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        {/* 추후 다른 라우트 추가 예정 */}
      </Routes>
    </Router>
  );
}
export default App;
