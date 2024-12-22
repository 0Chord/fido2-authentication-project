import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Key, Fingerprint } from 'lucide-react';
import useUserStore from '../stores/useUserStore';

const LoginPage = () => {
  const navigate = useNavigate();
  const setUserInfo = useUserStore((state) => state.setUserInfo);

  const [loginData, setLoginData] = useState({
    email: '',
    password: ''
  });
  const [error, setError] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('/user/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(loginData)
      });

      const data = await response.json();

      if (response.ok && data.resultCode === 200) {
        setUserInfo(data.resultData);

        navigate('/mypage');
      } else {
        setError(data.resultMessage || '로그인에 실패했습니다');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('서버 통신 중 오류가 발생했습니다');
    }
  };

  const handleFIDOLogin = () => {
    console.log('FIDO login attempted');
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <div className="px-4 py-8 bg-white shadow">
        <h1 className="text-2xl font-bold text-center text-gray-900">
          로그인
        </h1>
      </div>

      <div className="flex-1 px-4 py-6">
        <form className="space-y-4" onSubmit={handleLogin}>
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}

          <div>
            <label htmlFor="email" className="block text-base font-medium text-gray-700 mb-1">
              이메일
            </label>
            <input
              id="email"
              name="email"
              type="email"
              required
              className="block w-full px-4 py-3 border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              value={loginData.email}
              onChange={handleInputChange}
              placeholder="example@email.com"
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-base font-medium text-gray-700 mb-1">
              비밀번호
            </label>
            <input
              id="password"
              name="password"
              type="password"
              required
              className="block w-full px-4 py-3 border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              value={loginData.password}
              onChange={handleInputChange}
            />
          </div>

          <button
            type="submit"
            className="mt-6 w-full flex items-center justify-center px-4 py-3 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <Key className="h-6 w-6 mr-2" />
            로그인
          </button>
        </form>

        <button
          onClick={handleFIDOLogin}
          className="mt-4 w-full flex items-center justify-center px-4 py-3 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
        >
          <Fingerprint className="h-6 w-6 mr-2" />
          FIDO 로그인
        </button>

        <button
          onClick={() => navigate('/signup')}
          className="mt-4 w-full flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg shadow-sm text-base font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          회원가입
        </button>

        <div className="mt-6 flex items-center justify-center space-x-4 text-sm text-gray-600">
          <button className="hover:text-indigo-500">아이디 찾기</button>
          <span className="text-gray-300">|</span>
          <button className="hover:text-indigo-500">비밀번호 찾기</button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;