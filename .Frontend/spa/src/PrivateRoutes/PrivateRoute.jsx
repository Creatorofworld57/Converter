import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";

const PrivateRoute = () => {
    const [authorize, setAuthorize] = useState(null); // Initializing with null for loading state

    const auth = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/authorization', {
                method: 'GET',
                credentials: 'include'
            });
            console.log('Authorization response status:', response.status);
            if (response.status === 200) {
                setAuthorize(true);
            } else {
                setAuthorize(false);
            }
        } catch (error) {
            console.error('Failed to fetch authorization:', error);
            setAuthorize(false);
        }
    };

    useEffect(() => {
        auth();
    }, []); // Only run on mount

    if (authorize === null) {
        return <div>Loading...</div>; // Show loading while waiting for authorization check
    }

    console.log('Authorize state:', authorize);

    return (
        authorize ? <Outlet /> : <Navigate to="/login" />
    );
};

export default PrivateRoute;
