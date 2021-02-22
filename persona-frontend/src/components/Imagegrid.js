import React,{useState} from 'react';
import { motion } from 'framer-motion';
import axios from 'axios';

const ImageGrid = ({ setSelectedImg }) => {
 
   axios.get(`http://localhost:2222/upload/suresh`)
      .then(res => {
        const doc = res.data;
        this.setState({ doc });
      })
  }

  return (
    <div className="img-grid">
      {docs && docs.map(doc => (
        <motion.div className="img-wrap" key={doc.id} 
          layout
          whileHover={{ opacity: 1 }}s
          onClick={() => setSelectedImg(doc.url)}
        >
          <motion.img src={doc.url} alt="uploaded pic"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 1 }}
          />
        </motion.div>
      ))}
    </div>
  )
}

export default ImageGrid;