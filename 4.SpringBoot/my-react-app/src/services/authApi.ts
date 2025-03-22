import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {APP_ENV} from "../env";
import {IUserLoginRequest, IUserRegisterRequest} from "../types/Auth.ts";


export const authApi = createApi({
    reducerPath: 'authApi', // Унікальний шлях для цього API у Redux Store
    baseQuery: fetchBaseQuery({ baseUrl: APP_ENV.REMOTE_BASE_API }),
    tagTypes: ["AuthUser"],
    endpoints: (builder) => ({

        registerUser: builder.mutation<void, IUserRegisterRequest>({
            query: (userRegister) => ({
                url: "auth/register",
                method: "POST",
                body: userRegister,
            }),
            //invalidatesTags: ["AuthUser"],
        }),

        loginUser: builder.mutation<string, IUserLoginRequest>({
            query: (userLogin) => ({
                url: "auth/login",
                method: "POST",
                body: userLogin,
            }),
            //invalidatesTags: ["AuthUser"],
        })

    }),
});

// Автоматично згенерований хук
export const {
    useRegisterUserMutation,
    useLoginUserMutation
} = authApi;