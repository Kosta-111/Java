import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { Category, ICategoryCreate, ICategoryEdit } from "../types/Category";
import { APP_ENV } from "../env";
import { serialize } from 'object-to-formdata';

export const categoriesApi = createApi({
    reducerPath: 'categoriesApi',
    baseQuery: fetchBaseQuery({ baseUrl: APP_ENV.REMOTE_BASE_API }),
    tagTypes: ["Category"], // Додаємо tag для категорій
    endpoints: (builder) => ({

        getAllCategories: builder.query<Category[], void>({
            query: () => 'categories',
            providesTags: ["Category"]  // Позначаємо, що цей запит пов'язаний з "Category"
        }),

        getCategoryById: builder.query<Category, string>({
            query: (id) => `categories/${id}`,
            providesTags: ["Category"]
        }),

        createCategory: builder.mutation<Category, ICategoryCreate>({
            query: (model) => {
                try {
                    const formData = serialize(model);
                    return {
                        url: 'categories',
                        method: 'POST',
                        body: formData
                    };
                } catch {
                    throw new Error("Error serializing the form data.");
                }
            },
            invalidatesTags: ["Category"]   // Інвалідовуємо "Category" після створення
        }),

        updateCategory: builder.mutation<void, ICategoryEdit>({
            query: ({ id, ...model }) => {
                try {
                    const formData = serialize(model);
                    return {
                        url: `categories/${id}`,
                        method: 'PUT',
                        body: formData
                    };
                } catch {
                    throw new Error("Error serializing the form data.");
                }
            },
            invalidatesTags: ["Category"]
        }),

        deleteCategory: builder.mutation<void, number>({
            query: (id) => ({
                url: `categories/${id}`,
                method: 'DELETE'
            }),
            invalidatesTags: ["Category"]
        }),
    }),
});

export const {
    useGetAllCategoriesQuery,
    useGetCategoryByIdQuery,
    useCreateCategoryMutation,
    useUpdateCategoryMutation,
    useDeleteCategoryMutation
} = categoriesApi;