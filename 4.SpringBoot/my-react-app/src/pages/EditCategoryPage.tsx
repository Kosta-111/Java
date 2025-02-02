import React, { useEffect, useState } from 'react';
import { useGetCategoryByIdQuery, useUpdateCategoryMutation } from '../services/categoriesApi';
import { useNavigate, useParams } from 'react-router-dom';
import { ICategoryEdit } from "../types/Category";

const EditCategoryPage: React.FC = () => {
    const { id } = useParams<{ id: string }>(); // Отримуємо ID категорії з URL

    const [categoryUpdated, setCategoryUpdated] = useState<ICategoryEdit>({
        id: 0,
        name: '',
        description: '',
        imageFile: null
    });
    const { data: categoryData, isLoading: isLoadingCategory, error: getCategoryError } = useGetCategoryByIdQuery(id!); // Отримуємо категорію
    const [updateCategory, { isLoading, error }] = useUpdateCategoryMutation();

    const navigate = useNavigate();

    useEffect(() => {
        if (categoryData) { // Коли категорія завантажена, оновлюємо стейт
            setCategoryUpdated({
                id: categoryData.id,
                name: categoryData.name,
                description: categoryData.description ?? '',
                imageFile: null
            });
        }
    }, [categoryData]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            // Викликаємо мутацію для редагування категорії
            await updateCategory(categoryUpdated).unwrap();
            navigate('..'); // Перехід до списку категорій
        } catch (err) {
            console.error('Error updating category:', err);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setCategoryUpdated((prevCategory) => ({
            ...prevCategory,
            [name]: value,
        }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            const file = e.target.files[0];
            setCategoryUpdated((prevCategory) => ({
                ...prevCategory,
                imageFile: file,
            }));
        }
    };

    if (isLoadingCategory) return <p>Loading...</p>;
    if (getCategoryError) return <p>Error loading category data.</p>;

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-2xl font-bold text-center mb-6">Edit Category</h1>
            <button onClick={() => navigate(-1)}
                className="px-4 py-2 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-700 mb-4"
            >
                Go Back
            </button>

            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="name">
                        Category Name
                    </label>
                    <input
                        id="name"
                        name="name"
                        type="text"
                        value={categoryUpdated.name}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="description">
                        Description
                    </label>
                    <textarea
                        id="description"
                        name="description"
                        value={categoryUpdated.description}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                        rows={4}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="imageFile">
                        Category Image
                    </label>
                    <input
                        id="imageFile"
                        name="imageFile"
                        type="file"
                        onChange={handleFileChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                    />
                </div>

                <div className="flex justify-center">
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="bg-blue-500 text-white p-2 rounded w-full md:w-1/2 mt-4"
                    >
                        {isLoading ? 'Updating...' : 'Update Category'}
                    </button>
                </div>

                {error && <p className="text-red-500 mt-2">Error updating category!</p>}
            </form>
        </div>
    );
};

export default EditCategoryPage;