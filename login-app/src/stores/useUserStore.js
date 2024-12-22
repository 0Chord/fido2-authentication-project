import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useUserStore = create(
  persist(
    (set) => ({
      token: null,
      userId: null,
      email: null,
      nickname: null,

      setUserInfo: (userData) =>
        set({
          token: userData.token,
          userId: userData.userId,
          email: userData.email,
          nickname: userData.nickname,
        }),

      clearUserInfo: () =>
        set({
          token: null,
          userId: null,
          email: null,
          nickname: null,
        }),
    }),
    {
      name: 'user-storage',
    }
  )
);

export default useUserStore;