CREATE TABLE product_raw_materials (
                                       id BIGSERIAL PRIMARY KEY,
                                       product_id BIGINT NOT NULL,
                                       raw_material_id BIGINT NOT NULL,
                                       required_quantity INTEGER NOT NULL,

                                       CONSTRAINT fk_prm_product
                                           FOREIGN KEY (product_id)
                                               REFERENCES products (id),

                                       CONSTRAINT fk_prm_raw_material
                                           FOREIGN KEY (raw_material_id)
                                               REFERENCES raw_materials (id)
);
