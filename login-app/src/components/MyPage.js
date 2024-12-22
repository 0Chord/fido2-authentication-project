import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {KeyRound, LogOut, UserCircle} from 'lucide-react';
import useUserStore from '../stores/useUserStore';

const MyPage = () => {
  const navigate = useNavigate();
  const {nickname, email, token, clearUserInfo} = useUserStore();

  useEffect(() => {
    if (!email) {
      navigate('/');
    }
  }, [email, navigate]);

  const handleFIDORegistration = async () => {
    try {
      // 1. 서버에서 challenge 받기
      const challenge = await fetch('/attestation/options', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: email,
          displayName: nickname,
          authenticatorSelection: {
            requireResidentKey: false,
            authenticatorAttachment: "platform",
            userVerification: "preferred"
          },
          attestation: "direct"
        }),
        credentials: 'include',
      });

      if (!challenge.ok) {
        throw new Error('Failed to get challenge');
      }

      const challengeData = await challenge.json();

      // 2. FIDO2 등록 시작
      const base64ToArrayBuffer = (base64) => {
        const binaryString = window.atob(base64);
        const bytes = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
          bytes[i] = binaryString.charCodeAt(i);
        }
        return bytes.buffer;
      };

      // Challenge 데이터 변환
      const publicKeyCredentialCreationOptions = {
        ...challengeData,
        challenge: base64ToArrayBuffer(challengeData.challenge),
        user: {
          ...challengeData.user,
          id: base64ToArrayBuffer(challengeData.user.id),
        },
      };

      const credential = await navigator.credentials.create({
        publicKey: publicKeyCredentialCreationOptions
      });

      // ArrayBuffer를 Base64 문자열로 변환하는 함수
      const arrayBufferToBase64 = (buffer) => {
        const bytes = new Uint8Array(buffer);
        let binary = '';
        for (let i = 0; i < bytes.byteLength; i++) {
          binary += String.fromCharCode(bytes[i]);
        }
        return window.btoa(binary);
      };

      console.log(credential.id, arrayBufferToBase64(credential.response.clientDataJSON), arrayBufferToBase64(credential.response.attestationObject), credential.type)
      // 3. 서버에 등록 완료 요청
      const response = await fetch('/attestation/result', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        credentials: 'include',
        body: JSON.stringify({
          id: credential.id,
          response: {
            clientDataJSON: arrayBufferToBase64(credential.response.clientDataJSON),
            attestationObject: arrayBufferToBase64(credential.response.attestationObject)
          },
          type: credential.type
        }),
      });

      if (response.ok) {
        alert('FIDO2 등록이 완료되었습니다.');
      } else {
        throw new Error('Failed to register FIDO2');
      }
    } catch (error) {
      console.error('FIDO2 registration error:', error);
      alert('FIDO2 등록 중 오류가 발생했습니다.');
    }
  };

  const handleLogout = () => {
    clearUserInfo();
    navigate('/');
  };

  if (!email) {
    return null;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 py-6">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-gray-900">마이페이지</h1>
            <button
              onClick={handleLogout}
              className="flex items-center text-gray-600 hover:text-gray-900"
            >
              <LogOut className="h-5 w-5 mr-1"/>
              로그아웃
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-4 py-6">
        <div className="bg-white shadow rounded-lg">
          {/* Profile Section */}
          <div className="p-6 border-b border-gray-200">
            <div className="flex items-center">
              <UserCircle className="h-16 w-16 text-gray-400"/>
              <div className="ml-4">
                <h2 className="text-xl font-bold text-gray-900">{nickname}</h2>
                <p className="text-gray-600">{email}</p>
              </div>
            </div>
          </div>

          {/* FIDO2 Section */}
          <div className="p-6">
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <KeyRound className="h-6 w-6 text-gray-400"/>
                <div className="ml-3">
                  <h3 className="text-lg font-medium text-gray-900">FIDO2 등록</h3>
                  <p className="text-sm text-gray-500">
                    생체 인증 또는 PIN을 사용하여 더 안전하게 로그인하세요
                  </p>
                </div>
              </div>
              <button
                onClick={handleFIDORegistration}
                className="flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                FIDO2 등록
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyPage;