import { useEffect, useState } from "react";
import axios from "axios";
import "./home.css"; // Optional: styling

const Home = () => {
  const [products, setProducts] = useState([]);
  const [addedToCartProducts, setaddedToCartProducts] = useState([]);
  const [balance, setBalance] = useState(0);
  const [message, setMessage] = useState("");
  const [exchangehandlerMessage, setExchangehandlerMessage] = useState("");

  const [isExchangeBoxShowed, setIsExchangeBoxShowed] = useState(false);
  const [isPanierVisible, setIsPanierVisible] = useState(false);

  const [coin, setCoin] = useState(0);

  const validCoins = [0.25, 0.5, 1, 2, 5, 10]; // Valid coin denominations

  // Refetch when balance changes

  const fetchBalance = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/balance");
      console.log("Current balance:", response.data);
      setBalance(response.data);
    } catch (error) {
      console.error("Failed to fetch balance:", error);
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/products");
      setProducts(res.data);
    } catch (error) {
      console.error("Failed to fetch products:", error);
    }
  };

  const handleInsertCoin = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/insert",
        coin,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      alert("New balance: " + response.data);
      setBalance(response.data);
    } catch (error) {
      console.error("Failed to insert coin:", error);
    }
  };

  const handleExtractCoin = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/extract",
        coin,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      alert("New balance: " + response.data);
      setBalance(response.data);
    } catch (error) {
      alert(error.response.data);
    }
  };
  const handleExtractProductPrice = async (productPrice) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/extract-product-price",
        productPrice,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      alert("New balance: " + response.data);
      setBalance(response.data);
    } catch (error) {
      alert(error.response.data);
    }
  };
  const handleAddProductPrice = async (productPrice, productId) => {
    try {
      const requestData = {
        productPrice: productPrice,
        productId: productId,
      };
      const response = await axios.post(
        "http://localhost:8080/api/add-product-price",
        requestData,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      alert("New balance: " + response.data);
      setBalance(response.data);
    } catch (error) {
      alert(error.response.data);
    }
  };

  const handlePurchasedItemsList = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/list-added-to-cart"
      );
      console.log(response.data);
      setaddedToCartProducts(response.data);
    } catch (error) {
      console.error("Failed to fetch products:", error);
    }
  };

  const handlePurchase = async (productId) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/add-to-cart",
        productId,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      alert("New balance: " + response.data);
      setBalance(response.data);
    } catch (error) {
      alert(error.response.data);
    }
  };

  const handleResetBalance = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/reset-balance"
      );
      setBalance(response.data);
    } catch (error) {
      console.error("Failed to reset balance:", error);
      setMessage("Failed to reset balance");
    }
  };

  const subtractQuantity = async (productId) => {
    try {
      await axios.post(
        "http://localhost:8080/api/subtract-quantity",
        productId,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
    } catch (error) {
      console.error("Failed to subtract quantity:", error);
    }
  };

  const handleEmptyPanier = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/remove-all-cart-items"
      );
      alert(response.data);
    } catch (error) {
      console.error("Failed to remove panier", error);
    }
  };
  const exchangeHandler = async () => {
    setIsExchangeBoxShowed(true);
    try {
      const response = await axios.get(
        "http://localhost:8080/api/return-exchange"
      );
      console.log(response.data);
      setExchangehandlerMessage(response.data);
    } catch (error) {
      console.error("Failed to exchange ", error);
    }
  };
  useEffect(() => {
    fetchBalance();
    fetchProducts();
  }, [balance]);

  return (
    <div className="home-container">
      <h1>Vending Machine</h1>

      <div className="balance-section">
        <h3>Balance: {balance.toFixed(2)} MAD</h3>
        <div className="valid-coins-section">
          <h2>Valid coins are: </h2>
          <div className="valid-coins">
            {validCoins.map((coinValue) => (
              <p key={coinValue}>{coinValue} MAD |</p>
            ))}
          </div>
        </div>
        <div className="coins">
          <form action="get" onSubmit={(e) => e.preventDefault()}>
            <input
              type="number"
              placeholder="Insert coin"
              value={coin}
              onChange={(e) => setCoin(e.target.value)}
            />
            <button
              type="button"
              onClick={async () => {
                await handleInsertCoin();
                isExchangeBoxShowed ? await exchangeHandler() : null;
              }}
            >
              Insert Coin
            </button>
            <button
              type="button"
              onClick={async () => {
                await handleExtractCoin();
                isExchangeBoxShowed ? await exchangeHandler() : null;
              }}
            >
              Substract Coin
            </button>
            <button
              type="button"
              onClick={async () => {
                await handleResetBalance();
                isExchangeBoxShowed ? await exchangeHandler() : null;
              }}
            >
              Reset Balance
            </button>
            <button
              type="button"
              onClick={async () => {
                await exchangeHandler();
                await exchangeHandler();
              }}
            >
              Exchange return
            </button>
          </form>
        </div>
      </div>
      <div className="exchange-section">
        <div className="exchange-text-box">
          <h3>Change to Return</h3>
          <button onClick={() => setIsExchangeBoxShowed(false)}>close</button>
        </div>
        <div className="exchange-info-box">
          {isExchangeBoxShowed ? <pre>{exchangehandlerMessage}</pre> : null}
        </div>
      </div>
      {message && <p className="message">{message}</p>}

      <div className="products-section">
        <h2>Available Products</h2>
        <div className="product-list">
          {products.map((product) => (
            <div key={product.id} className="product-card">
              <h4>{product.name}</h4>
              <p>Price: {product.price} MAD</p>
              <button
                onClick={async () => {
                  await handlePurchase(product.id);
                  await handlePurchasedItemsList();
                  isExchangeBoxShowed ? await exchangeHandler() : null;
                }}
                disabled={!product.purchasable}
              >
                Buy
              </button>
            </div>
          ))}
        </div>
      </div>
      <div>
        <h3>Summary</h3>
        <button
          onClick={async () => {
            await handlePurchasedItemsList();
            setIsPanierVisible(true);
          }}
        >
          Show Panier
        </button>
        <button
          onClick={() => {
            setIsPanierVisible(false);
          }}
        >
          Hide Panier
        </button>
        <button
          onClick={async () => {
            await handleEmptyPanier();
            await handlePurchasedItemsList();
          }}
        >
          Empty Panier
        </button>
        {isPanierVisible ? (
          <div className="product-list">
            {addedToCartProducts.map((product, id) => (
              <div key={id} className="added-to-cart-products">
                <h4>{product.name}</h4>
                <p>Price: {product.price} MAD</p>
                <p>Quantity: {product.quantity} Piece</p>
                {console.log(product)}
                <div>
                  <button
                    onClick={async () => {
                      await handlePurchase(product.id);
                      await handlePurchasedItemsList();
                      isExchangeBoxShowed ? await exchangeHandler() : null;
                    }}
                  >
                    +
                  </button>
                  <button
                    onClick={async () => {
                      await handleAddProductPrice(product.price, product.id);
                      await subtractQuantity(product.id);
                      await handlePurchasedItemsList();
                      isExchangeBoxShowed ? await exchangeHandler() : null;
                    }}
                  >
                    -
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <></>
        )}
      </div>
    </div>
  );
};

export default Home;
