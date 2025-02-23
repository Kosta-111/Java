import React, { useState } from 'react';
import { useCreateProductMutation } from "../services/productsApi.ts";
import { useNavigate } from 'react-router-dom';
import { IProductCreate } from "../types/Product.ts";
import {useGetAllCategoriesQuery} from "../services/categoriesApi.ts";

const CreateProductPage: React.FC = () => {
    const [product, setProduct] = useState<IProductCreate>({
        name: '',
        description: '',
        price: 0,
        amount: 0,
        categoryId: 0,
        imageFiles: null
    });
    const { data: categories, isLoading: categoriesLoading, error: categoriesError } = useGetAllCategoriesQuery();
    const [createProduct, { isLoading, error }] = useCreateProductMutation();

    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (!product.imageFiles) {
                // @ts-ignore
                delete product.imageFiles;
            }
            // Викликаємо мутацію для створення продукту
            await createProduct(product).unwrap();
            navigate('..'); // Перехід до нового продукту
        } catch (err) {
            console.error('Error creating product:', err);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setProduct((prevProduct) => ({
            ...prevProduct,
            [name]: value,
        }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            setProduct((prevProduct) => ({
                ...prevProduct,
                imageFiles: e.target.files,
            }));
        }
    };

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-2xl font-bold text-center mb-6">Create Product</h1>
            <button onClick={() => navigate(-1)}
                    className="px-4 py-2 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-700 mb-4"
            >
                Go Back
            </button>

            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="name">
                        Product Name
                    </label>
                    <input
                        id="name"
                        name="name"
                        type="text"
                        value={product.name}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="categoryId">
                        Category
                    </label>
                    {categoriesLoading ? (
                        <p>Loading categories...</p>
                    ) : categoriesError ? (
                        <p className="text-red-500">Failed to load categories</p>
                    ) : (
                        <select
                            id="categoryId"
                            name="categoryId"
                            value={product.categoryId}
                            onChange={handleChange}
                            className="w-full p-2 border border-gray-300 rounded mt-2"
                            required
                        >
                            <option value={0} disabled>Select Category</option>
                            {categories?.map((category) => (
                                <option key={category.id} value={category.id}>
                                    {category.name}
                                </option>
                            ))}
                        </select>
                    )}
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="price">
                        Price
                    </label>
                    <input
                        id="price"
                        name="price"
                        type="number"
                        min={0}
                        step={0.01}
                        value={product.price}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="amount">
                        Amount
                    </label>
                    <input
                        id="amount"
                        name="amount"
                        type="number"
                        min={0}
                        step={1}
                        value={product.amount}
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
                        value={product.description}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                        rows={4}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700" htmlFor="imageFile">
                        Product Images
                    </label>
                    <input
                        id="imageFile"
                        name="imageFile"
                        type="file"
                        multiple={true}
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
                        {isLoading ? 'Creating...' : 'Create Product'}
                    </button>
                </div>

                {error && <p className="text-red-500 mt-2">Error creating product!</p>}
            </form>
        </div>
    );
};

export default CreateProductPage;