import { createContext } from "react";

// ✅ PUT IT HERE (top of file)
export type AuthContextType = {
  token: string | null;
  role: string | null;
  login: (token: string) => void;
  logout: () => void;
};

// ✅ use it here
export const AuthContext = createContext<AuthContextType | undefined>(undefined);