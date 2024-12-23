import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/EmailInputPage';
import SignUpPage from "./components/SignUpPage";
import MyPage from "./components/MyPage";
import EmailInputPage from "./components/EmailInputPage";
import AuthPage from "./components/AuthPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<EmailInputPage />} />
        <Route path="/auth" element={<AuthPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/mypage" element={<MyPage />} />
        {/* 추후 다른 라우트 추가 예정 */}
      </Routes>
    </Router>
  );
}
export default App;
