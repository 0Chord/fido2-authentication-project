import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import SignUpPage from "./components/SignUpPage";
import MyPage from "./components/MyPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/mypage" element={<MyPage />} />
        {/* 추후 다른 라우트 추가 예정 */}
      </Routes>
    </Router>
  );
}
export default App;
