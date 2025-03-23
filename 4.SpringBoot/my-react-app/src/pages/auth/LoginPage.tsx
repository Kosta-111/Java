import React from "react";
import {Button, Form, Input} from "antd";
import {GoogleOAuthProvider} from "@react-oauth/google";
import { GoogleOutlined } from '@ant-design/icons';
import {IUserLoginRequest} from "../../types/Auth.ts";
import GoogleLoginButton from "./GoogleLoginButton.tsx";
import {useNavigate} from "react-router-dom";
import {useGoogleAuthUserMutation, useLoginUserMutation} from "../../services/authApi.ts";

const {Item} = Form;

const LoginPage: React.FC = () => {

    const [form] = Form.useForm<IUserLoginRequest>();
    const navigate = useNavigate();
    const [loginUser] = useLoginUserMutation();
    const [googleAuthUser] = useGoogleAuthUserMutation();

    const onFinish = async (values: IUserLoginRequest) => {
        try {
            console.log("Login user", values);
            const response = await loginUser(values).unwrap();
            const token = response.token;
            if (token) {
                console.log("Login success, jwt token: ", token);
                localStorage.setItem('jwt', token);
            }
            navigate("..");
        } catch (error) {
            console.error("Login error", error);
        }
    }

    const onLoginGoogleResult = async (tokenGoogle: string) => {
        try {
            console.log("Google token", tokenGoogle);
            const response = await googleAuthUser({token: tokenGoogle}).unwrap();
            const token = response.token;
            if (token) {
                console.log("Google login success, jwt token: ", token);
                localStorage.setItem('jwt', token);
            }
            navigate("..");
        } catch (error) {
            console.error("Login error", error);
        }
    }

    return (
        <>
            <GoogleOAuthProvider clientId={"688315354046-isd3q5qkjaj88uaj9oudrldsf18bm592.apps.googleusercontent.com"}>
                <h1 className={"text-center text-4xl font-bold text-blue-500"}>Login page</h1>

                <div style={{maxWidth: '400px', margin: '0 auto'}}>
                    <Form
                        form={form}
                        onFinish={onFinish}
                        layout="vertical">

                        <Item
                            name="username"
                            label={"E-mail"}
                            rules={[
                                { required: true, message: "Enter your email" },
                                { type: "email", message: "Enter correct email" }
                            ]}>
                            <Input placeholder={"E-mail"}/>
                        </Item>

                        <Item
                            name="password"
                            label="Password"
                            rules={[
                                { required: true, message: "Enter password" },
                                { min: 6, message: "Password must be at least 6 characters" }
                            ]}
                        >
                            <Input.Password placeholder="Password"/>
                        </Item>

                        <Item>
                            <Button type="primary" htmlType="submit">
                                Login
                            </Button>
                        </Item>

                        <GoogleLoginButton icon={<GoogleOutlined />} title='Login with Google' onLogin={onLoginGoogleResult} />

                    </Form>
                </div>
            </GoogleOAuthProvider>
        </>
    )
}

export default LoginPage;