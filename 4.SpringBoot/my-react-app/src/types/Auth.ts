import {ReactNode} from "react";

export interface IUserRegisterRequest {
    username: string;
    password: string;
    imageFile: File | null;
}

export interface IUserLoginRequest {
    username: string;
    password: string;
}

export interface IGoogleAuthRequest {
    token: string;
}

export interface ILoginResult {
    token?: string;
    error?: string;
}

export interface LoginButtonProps {
    title: string
    onLogin: (token: string) => void
    icon: ReactNode
}