// ContextForAudio.js
import React, { useState } from 'react';

export const TypeContext = React.createContext();

const Context = (props) => {
    const [data, setData] = useState(""); // 'data' is the same as 'type'

    const info = {
        type: data, // here, we rename 'data' to 'type'
        setType: setData, // here, we rename 'setData' to 'setType'
    };

    return (
        <TypeContext.Provider value={info}>
            {props.children}
        </TypeContext.Provider>
    );
};

export default Context;
