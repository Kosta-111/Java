import React from "react";
import {Button, Form, Input, Upload} from "antd";
import {useNavigate} from "react-router-dom";
import {useRegisterUserMutation} from "../../services/authApi.ts";
import {IUserRegisterRequest} from "../../types/Auth.ts";
import {UploadOutlined} from "@ant-design/icons";

const {Item} = Form;

const RegisterPage : React.FC = () => {

    const [form] = Form.useForm<IUserRegisterRequest>();
    const navigate = useNavigate();
    const [registerUser] = useRegisterUserMutation();

    const onFinish = async (values: IUserRegisterRequest) => {
        try {
            console.log("Register user", values);
            const response = await registerUser(values).unwrap();
            console.log("User registered successfully", response);
            navigate("..");
        } catch (error) {
            console.error("Registration error", error);
        }
    }

    return (
        <>
            <h1 className={"text-center text-4xl font-bold text-blue-500"}>Registration page</h1>

            <div style={ {maxWidth:'400px', margin:'0 auto'}}>
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
                        <Input.Password placeholder="Password" />
                    </Item>

                    <Item
                        name="confirmPassword"
                        label="Confirm password"
                        dependencies={["password"]}
                        rules={[
                            { required: true, message: "Repeat password" },
                            ({ getFieldValue }) => ({
                                validator(_, value) {
                                    if (!value || getFieldValue("password") === value) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error("Passwords not matched"));
                                },
                            }),
                        ]}
                    >
                        <Input.Password placeholder="Repeat password" />
                    </Item>

                    <Item
                        name="imageFile"
                        label="Upload File"
                        valuePropName="file"
                        getValueFromEvent={e => e?.file}
                    >
                        <Upload
                            listType="picture-card"
                            beforeUpload={() => false}
                            maxCount={1}
                            accept="image/*"
                            showUploadList={{ showPreviewIcon: true }}
                        >
                            <UploadOutlined />
                        </Upload>
                    </Item>

                    <Item>
                        <Button type="primary" htmlType="submit">
                            Confirm registration
                        </Button>
                    </Item>
                </Form>
            </div>
        </>
    )
}

export default RegisterPage;