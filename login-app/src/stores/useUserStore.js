import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useUserStore = create(
  persist(
    (set) => ({
      token: null,
      userId: null,
      email: null,
      nickname: null,
      tempEmail: null,

      setTempEmail: (email) => set({ tempEmail: email }),
      clearTempEmail: () => set({ tempEmail: null }),

      setUserInfo: (userData) =>
        set({
          token: userData.token,
          userId: userData.userId,
          email: userData.email,
          nickname: userData.nickname,
          tempEmail: null,
        }),

      clearUserInfo: () =>
        set({
          token: null,
          userId: null,
          email: null,
          nickname: null,
          tempEmail: null,
        }),
    }),
    {
      name: 'user-storage',
    }
  )
);

export default useUserStore;