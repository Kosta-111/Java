import React, { useState } from 'react';
import { useCreateCategoryMutation } from '../services/categoriesApi';
import { useNavigate } from 'react-router-dom';
import { ICategoryCreate } from "../types/Category";

const CreateCategoryPage: React.FC = () => {
    const [category, setCategory] = useState<ICategoryCreate>({
        name: '',
        description: '',
        imageFile: null
    });

    const [createCategory, { isLoading, error }] = useCreateCategoryMutation();

    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (!category.imageFile) {
                // @ts-ignore
                delete category.imageFile;
            }
            // Викликаємо мутацію для створення категорії
            await createCategory(category).unwrap();
            navigate('..'); // Перехід до нової категорії
        } catch (err) {
            console.error('Error creating category:', err);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setCategory((prevCategory) => ({
            ...prevCategory,
            [name]: value,
        }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            const file = e.target.files[0];
            setCategory((prevCategory) => ({
                ...prevCategory,
                imageFile: file,
            }));
        }
    };

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-2xl font-bold text-center mb-6">Create Category</h1>
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
                        value={category.name}
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
                        value={category.description}
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
                        {isLoading ? 'Creating...' : 'Create Category'}
                    </button>
                </div>

                {error && <p className="text-red-500 mt-2">Error creating category!</p>}
            </form>
        </div>
    );
};

export default CreateCategoryPage;