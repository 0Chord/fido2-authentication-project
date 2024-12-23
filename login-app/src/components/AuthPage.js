import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Key, Fingerprint } from 'lucide-react';
import useUserStore from '../stores/useUserStore';

const AuthPage = () => {
  const navigate = useNavigate();
  const email = useUserStore((state) => state.tempEmail);
  const setUserInfo = useUserStore((state) => state.setUserInfo);

  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handlePasswordLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('/user/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          email,
          password
        })
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

  const handleFIDOLogin = async () => {
    try {
      // 1. Get challenge
      const optionsResponse = await fetch('/assertion/options', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: email,
          userVerification: "required"
        })
      });

      const options = await optionsResponse.json();

      if (options.status !== 'ok') {
        throw new Error(options.errorMessage || 'Failed to get challenge');
      }

      // 2. Create credentials
      const credential = await navigator.credentials.get({
        publicKey: {
          challenge: Uint8Array.from(atob(options.challenge), c => c.charCodeAt(0)),
          timeout: options.timeout,
          rpId: options.rpId,
          allowCredentials: options.allowCredentials.map(cred => ({
            id: Uint8Array.from(atob(cred.id), c => c.charCodeAt(0)),
            type: cred.type,
          })),
          userVerification: options.userVerification
        }
      });

      // 3. Send assertion result
      const assertionResponse = await fetch('/assertion/result', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          id: credential.id,
          response: {
            authenticatorData: btoa(String.fromCharCode(...new Uint8Array(credential.response.authenticatorData))),
            clientDataJSON: btoa(String.fromCharCode(...new Uint8Array(credential.response.clientDataJSON))),
            signature: btoa(String.fromCharCode(...new Uint8Array(credential.response.signature))),
            userHandle: credential.response.userHandle ? btoa(String.fromCharCode(...new Uint8Array(credential.response.userHandle))) : ""
          },
          type: credential.type
        })
      });

      const assertionResult = await assertionResponse.json();

      if (assertionResult.status === 'ok') {
        navigate('/mypage');
      } else {
        setError(assertionResult.errorMessage || 'FIDO 인증에 실패했습니다');
      }

    } catch (error) {
      console.error('FIDO Error:', error);
      setError('FIDO 인증 중 오류가 발생했습니다');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <div className="px-4 py-8 bg-white shadow">
        <h1 className="text-2xl font-bold text-center text-gray-900">
          인증하기
        </h1>
        <p className="mt-2 text-center text-gray-600">{email}</p>
      </div>

      <div className="flex-1 px-4 py-6">
        <form className="space-y-4" onSubmit={handlePasswordLogin}>
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}

          <div>
            <label htmlFor="password" className="block text-base font-medium text-gray-700 mb-1">
              비밀번호
            </label>
            <input
              id="password"
              type="password"
              required
              className="block w-full px-4 py-3 border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button
            type="submit"
            className="mt-6 w-full flex items-center justify-center px-4 py-3 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <Key className="h-6 w-6 mr-2" />
            비밀번호로 로그인
          </button>
        </form>

        <button
          onClick={handleFIDOLogin}
          className="mt-4 w-full flex items-center justify-center px-4 py-3 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
        >
          <Fingerprint className="h-6 w-6 mr-2" />
          FIDO 로그인
        </button>

        <div className="mt-6 flex items-center justify-center space-x-4 text-sm text-gray-600">
          <button className="hover:text-indigo-500" onClick={() => navigate('/')}>이메일 변경</button>
          <span className="text-gray-300">|</span>
          <button className="hover:text-indigo-500">비밀번호 찾기</button>
        </div>
      </div>
    </div>
  );
};

export default AuthPage;