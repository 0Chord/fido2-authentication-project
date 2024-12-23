import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useUserStore from '../stores/useUserStore';

const EmailInputPage = () => {
  const navigate = useNavigate();
  const setEmail = useUserStore((state) => state.setTempEmail);

  const [emailInput, setEmailInput] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!emailInput.includes('@')) {
      setError('유효한 이메일을 입력해주세요');
      return;
    }

    setEmail(emailInput);
    navigate('/auth');
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <div className="px-4 py-8 bg-white shadow">
        <h1 className="text-2xl font-bold text-center text-gray-900">
          로그인
        </h1>
      </div>

      <div className="flex-1 px-4 py-6">
        <form className="space-y-4" onSubmit={handleSubmit}>
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
              type="email"
              required
              className="block w-full px-4 py-3 border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              value={emailInput}
              onChange={(e) => setEmailInput(e.target.value)}
              placeholder="example@email.com"
            />
          </div>

          <button
            type="submit"
            className="mt-6 w-full flex items-center justify-center px-4 py-3 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            다음
          </button>
        </form>

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

export default EmailInputPage;