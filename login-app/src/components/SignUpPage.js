import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export const SignUpPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    rePassword: '',
    nickname: ''
  });
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};

    // 이메일 검증
    if (!formData.email) {
      newErrors.email = '이메일을 입력해주세요';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = '올바른 이메일 형식이 아닙니다';
    }

    // 비밀번호 검증
    if (!formData.password) {
      newErrors.password = '비밀번호를 입력해주세요';
    } else if (formData.password.length < 8) {
      newErrors.password = '비밀번호는 8자 이상이어야 합니다';
    }

    // 비밀번호 확인 검증
    if (formData.password !== formData.rePassword) {
      newErrors.rePassword = '비밀번호가 일치하지 않습니다';
    }

    // 닉네임 검증
    if (!formData.nickname) {
      newErrors.nickname = '닉네임을 입력해주세요';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      const response = await fetch('/user/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        alert('회원가입이 완료되었습니다');
        navigate('/'); // 로그인 페이지로 이동
      } else {
        const errorData = await response.json();
        alert(errorData.message || '회원가입 중 오류가 발생했습니다');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('서버 통신 중 오류가 발생했습니다');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <div className="px-4 py-8 bg-white shadow">
        <h1 className="text-2xl font-bold text-center text-gray-900">
          회원가입
        </h1>
      </div>

      <div className="flex-1 px-4 py-6">
        <form className="space-y-4" onSubmit={handleSubmit}>
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
              value={formData.email}
              onChange={handleChange}
            />
            {errors.email && (
              <p className="mt-1 text-sm text-red-600">{errors.email}</p>
            )}
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
              value={formData.password}
              onChange={handleChange}
            />
            {errors.password && (
              <p className="mt-1 text-sm text-red-600">{errors.password}</p>
            )}
          </div>

          <div>
            <label htmlFor="rePassword" className="block text-base font-medium text-gray-700 mb-1">
              비밀번호 확인
            </label>
            <input
              id="rePassword"
              name="rePassword"
              type="password"
              required
              className="block w-full px-4 py-3 border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              value={formData.rePassword}
              onChange={handleChange}
            />
            {errors.rePassword && (
              <p className="mt-1 text-sm text-red-600">{errors.rePassword}</p>
            )}
          </div>

          <div>
            <label htmlFor="nickname" className="block text-base font-medium text-gray-700 mb-1">
              닉네임
            </label>
            <input
              id="nickname"
              name="nickname"
              type="text"
              required
              className="block w-full px-4 py-3 border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              value={formData.nickname}
              onChange={handleChange}
            />
            {errors.nickname && (
              <p className="mt-1 text-sm text-red-600">{errors.nickname}</p>
            )}
          </div>

          <button
            type="submit"
            className="mt-6 w-full flex items-center justify-center px-4 py-3 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            회원가입
          </button>

          <button
            type="button"
            onClick={() => navigate('/')}
            className="mt-4 w-full flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg shadow-sm text-base font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            돌아가기
          </button>
        </form>
      </div>
    </div>
  );
};

export default SignUpPage;