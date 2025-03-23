import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {APP_ENV} from "../env";
import {IGoogleAuthRequest, ILoginResult, IUserLoginRequest, IUserRegisterRequest} from "../types/Auth.ts";
import {serialize} from "object-to-formdata";


export const authApi = createApi({
    reducerPath: 'authApi', // Унікальний шлях для цього API у Redux Store
    baseQuery: fetchBaseQuery({ baseUrl: APP_ENV.REMOTE_BASE_API }),
    tagTypes: ["AuthUser"],
    endpoints: (builder) => ({

        registerUser: builder.mutation<void, IUserRegisterRequest>({
            query: (userRegister) => {
                try {
                    const formData = serialize(userRegister);
                    return {
                        url: 'auth/register',
                        method: 'POST',
                        body: formData
                    };
                } catch {
                    throw new Error("Error serializing the form data.");
                }
            },
            //invalidatesTags: ["AuthUser"],
        }),

        loginUser: builder.mutation<ILoginResult, IUserLoginRequest>({
            query: (userLogin) => ({
                url: "auth/login",
                method: "POST",
                body: userLogin,
            }),
            //invalidatesTags: ["AuthUser"],
        }),

        googleAuthUser: builder.mutation<ILoginResult, IGoogleAuthRequest>({
            query: (googleAuth) => ({
                url: "auth/google",
                method: "POST",
                body: googleAuth,
            }),
            //invalidatesTags: ["AuthUser"],
        }),

    }),
});

// Автоматично згенерований хук
export const {
    useRegisterUserMutation,
    useLoginUserMutation,
    useGoogleAuthUserMutation
} = authApi;