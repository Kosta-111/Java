import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import HomePage from './pages/HomePage';
import CategoriesPage from './pages/category/CategoriesPage.tsx';
import CreateCategoryPage from "./pages/category/CreateCategoryPage.tsx";
import EditCategoryPage from './pages/category/EditCategoryPage.tsx';
import ProductsPage from "./pages/product/ProductsPage.tsx";
import CreateProductPage from "./pages/product/CreateProductPage.tsx";
import EditProductPage from "./pages/product/EditProductPage.tsx";

const App: React.FC = () => (
  <Router>
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<HomePage />} />
        <Route path="categories" >
          <Route index element={<CategoriesPage />} />
          <Route path="create" element={<CreateCategoryPage />} />
          <Route path="edit/:id" element={<EditCategoryPage />} />
        </Route>
        <Route path="products" >
          <Route index element={<ProductsPage />} />
          <Route path="create" element={<CreateProductPage />} />
          <Route path="edit/:id" element={<EditProductPage />} />
        </Route>
      </Route>
    </Routes>
  </Router>
);

export default App;
