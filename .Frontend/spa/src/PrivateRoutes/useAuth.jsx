import { useState, useEffect } from 'react';

const useAuth = () => {
    const [auth, setAuth] = useState(false);

    useEffect(() => {
        const fetchAuth = async () => {
            try {
                const response = await fetch('https://localhost:8080/api/authorization', {
                    method: 'GET',
                    credentials: 'include'
                });

                if (response.status===200) {
                    setAuth(true);
                } else {
                    setAuth(false);
                }
            } catch (error) {
                setAuth(false);
            }
        };

        fetchAuth();
    }, []);

    return { auth };
};

export default useAuth;
